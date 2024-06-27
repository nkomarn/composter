package kyta.composter.server.world

import java.nio.file.Path
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kyta.composter.server.MinecraftServer
import kyta.composter.server.Tickable
import kyta.composter.server.world.storage.SqliteChunkStorage
import kyta.composter.world.BlockPos
import kyta.composter.world.World
import kyta.composter.world.dimension.DimensionType
import xyz.nkomarn.composter.world.generator.NoiseGenerator

class WorldManager(
    private val server: MinecraftServer,
    private val directory: Path,
) : Tickable {
    private val worlds = mutableMapOf<DimensionType, World>()
    val primaryWorld: World
        get() = worlds[DimensionType.OVERWORLD]!!

    fun load() {
        directory.toFile().mkdirs()

        // TODO temporarily create just 1 world
        val seed = ThreadLocalRandom.current().nextLong()
        val world = World(
            server,
            World.Properties(
                DimensionType.OVERWORLD,
                seed,
                SqliteChunkStorage(directory.resolve("world").also { it.toFile().mkdirs() }),
                NoiseGenerator(seed.toInt()),
                BlockPos(0, 62, 0),
            )
        )

        worlds[world.properties.type] = world
    }

    /*
     * tick all worlds in parallel.
     */
    override fun tick(currentTick: Long) {
        val latch = CountDownLatch(worlds.size)

        worlds.values.forEach {
            server.launch(Dispatchers.Default) {
                try {
                    it.tick(currentTick)
                } catch (x: Throwable) {
                    server.logger.error("encountered an error ticking world ${it.properties.type}", x)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
    }
}
