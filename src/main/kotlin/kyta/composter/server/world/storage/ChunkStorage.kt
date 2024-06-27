package kyta.composter.server.world.storage

import kyta.composter.world.ChunkPos
import kyta.composter.world.chunk.Chunk

interface ChunkStorage {
    suspend fun read(pos: ChunkPos): Chunk?
    suspend fun write(pos: ChunkPos, chunk: Chunk)
}
