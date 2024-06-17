package xyz.nkomarn.composter.world.generator

import kyta.composter.world.BlockPos
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
            for (y in 0..127) {
                for (z in 0..15) {
                    val state = when {
                        y == 1 -> BEDROCK
                        y == 5 -> GRASS_BLOCK
                        y < 5 -> DIRT
                        else -> AIR
                    }.defaultState

                    chunk.setBlock(BlockPos(x, y, z), state)
                }
            }
        }

        return chunk
    }
}
