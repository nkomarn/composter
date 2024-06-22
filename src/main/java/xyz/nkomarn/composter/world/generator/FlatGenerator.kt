package xyz.nkomarn.composter.world.generator

import kyta.composter.world.ChunkPos
import kyta.composter.world.block.AIR
import kyta.composter.world.block.BEDROCK
import kyta.composter.world.block.DIRT
import kyta.composter.world.block.GRASS_BLOCK
import kyta.composter.world.block.defaultState
import kyta.composter.world.chunk.Chunk

object FlatGenerator : WorldGenerator {
    override fun generate(xx: Int, zz: Int): Chunk {
        val chunk = Chunk(ChunkPos(xx, zz))

        for (x in 0..15) {
            for (y in 0..62) {
                for (z in 0..15) {
                    val state = when {
                        y <= 5 -> BEDROCK
                        y == 62 -> GRASS_BLOCK
                        y < 62 -> DIRT
                        else -> AIR
                    }.defaultState

                    chunk.setBlockRaw(x, y, z, state)
                }
            }
        }

        return chunk
    }
}
