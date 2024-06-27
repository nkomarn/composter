package kyta.composter.world.chunk

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kyta.composter.server.Tickable
import kyta.composter.world.ChunkPos
import kyta.composter.world.World
import kyta.composter.world.entity.Player
import kyta.composter.world.entity.blockPos

class ChunkController(private val world: World): Tickable {
    private val loadedChunks = ConcurrentHashMap<Long, Chunk>()
    private val pendingChunkLoads = ConcurrentHashMap.newKeySet<ChunkPos>()
    private var chunkActionsThisTick = 0

    fun getLoadedChunk(pos: ChunkPos): Chunk? {
        return loadedChunks[pos.compact]
    }

    fun isLoaded(pos: ChunkPos): Boolean {
        return loadedChunks.containsKey(pos.compact)
    }

    suspend fun getChunk(pos: ChunkPos): Chunk {
        if (isLoaded(pos)) {
            return getLoadedChunk(pos)!!
        }

        return withContext(Dispatchers.IO) {
            val chunk = world.properties.storage.read(pos)
                ?: world.properties.generator.generate(pos.x, pos.z)

            loadedChunks[pos.compact] = chunk
            return@withContext chunk
        }
    }

    suspend fun saveAll() {
        for (chunk in loadedChunks.values) {
            world.properties.storage.write(chunk.pos, chunk)
        }
    }

    override fun tick(currentTick: Long) {
        chunkActionsThisTick = 0

        for (entity in world.entities) {
            if (entity !is Player) continue

            val (originX, originZ) = ChunkPos(entity.blockPos)
            for (x in originX - SIMULATION_DISTANCE until originX + SIMULATION_DISTANCE) {
                for (z in originZ - SIMULATION_DISTANCE until originZ + SIMULATION_DISTANCE) {
                    if (chunkActionsThisTick >= MAX_CHUNK_ACTIONS_PER_TICK) {
                        break
                    }

                    val pos = ChunkPos(x, z)
                    if (!pendingChunkLoads.contains(pos) && !isLoaded(pos)) {
                        pendingChunkLoads.add(pos)

                        // schedule chunk load
                        world.server.launch {
                            getChunk(pos)
                            pendingChunkLoads.remove(pos)
                        }

                        chunkActionsThisTick++
                    }
                }
            }
        }

        if (currentTick % TICKS_CHUNK_SAVE == 0L) {
            world.server.logger.info("saving all chunks..")
            world.server.launch { saveAll() }
        }
    }

    private companion object {
        const val SIMULATION_DISTANCE = 8
        const val MAX_CHUNK_ACTIONS_PER_TICK = 8
        const val TICKS_CHUNK_SAVE = 600
    }
}