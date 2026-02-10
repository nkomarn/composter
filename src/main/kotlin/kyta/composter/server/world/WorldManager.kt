package kyta.composter.server.world

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kyta.composter.server.Tickable
import kyta.composter.world.BlockPos
import kyta.composter.world.World
import kyta.composter.world.dimension.DimensionType
import xyz.nkomarn.composter.world.ChunkIO
import xyz.nkomarn.composter.world.generator.NoiseGenerator
import java.nio.file.Path
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ThreadLocalRandom
import kyta.composter.server.MinecraftServer
import kyta.composter.world.ChunkPos
import kyta.composter.world.World.Companion.MAX_WORLD_HEIGHT
import kyta.composter.world.block.AIR
import kyta.composter.world.down
import kyta.composter.world.up

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
                ChunkIO(server, directory.resolve("world")),
                NoiseGenerator(seed.toInt()),
                BlockPos(0, 62, 0),
            )
        )

        world.createSpawnChunks()
        worlds[world.properties.dimensionType] = world
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
                    server.logger.error("encountered an error ticking world ${it.properties.dimensionType}", x)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()
    }
}

private fun World.createSpawnChunks() {
    runBlocking {
        for (x in -7..7) {
            for (z in -7..7) {
                chunks.getChunk(ChunkPos(x, z))
            }
        }
    }

    /*
     * find the lowest suitable spawn block.
     */
    var pos = BlockPos(0, MAX_WORLD_HEIGHT, 0)
    while (pos.y > 0) {
        val block = getBlock(pos).block
        if (block != AIR) break

        pos = pos.down(1)
    }

    properties.spawn = pos.up(1)
    server.logger.info(
        "the default world spawn is now ({}, {}, {})",
        properties.spawn.x,
        properties.spawn.y,
        properties.spawn.z
    )
}
