package kyta.composter.server.world.storage

import kyta.composter.world.ChunkPos
import kyta.composter.world.chunk.Chunk

object DummyChunkStorage : ChunkStorage {
    override suspend fun read(pos: ChunkPos) = null
    override suspend fun write(pos: ChunkPos, chunk: Chunk) {}
}