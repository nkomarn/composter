package xyz.nkomarn.composter.world.generator

import kyta.composter.world.ChunkPos
import kyta.composter.world.block.AIR
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.CACTUS
import kyta.composter.world.block.DANDELION
import kyta.composter.world.block.DIRT
import kyta.composter.world.block.FLOWING_WATER
import kyta.composter.world.block.GRASS_BLOCK
import kyta.composter.world.block.GRAVEL
import kyta.composter.world.block.SAND
import kyta.composter.world.block.SANDSTONE
import kyta.composter.world.block.SHORT_GRASS
import kyta.composter.world.block.STONE
import kyta.composter.world.block.WATER
import kyta.composter.world.block.defaultState
import kyta.composter.world.chunk.Chunk
import xyz.nkomarn.composter.world.noise.PerlinNoise
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.math.pow

class NoiseGenerator(private val seed: Int) : WorldGenerator {
    private val noise = PerlinNoise(seed)
    private val temperatureNoise = PerlinNoise(seed * 2)

    override fun generate(xx: Int, zz: Int): Chunk {
        val chunk = Chunk(ChunkPos(xx, zz))

        for (x in 0..15) {
            for (z in 0..15) {
                val blockX = (xx * 16) + x
                val blockZ = (zz * 16) + z

                var terrainNoiseValue = noise.noise(blockX / 20.0, blockZ / 20.0)

                /* create some large, abrupt cliffs - todo */
                /*
                if (terrainNoiseValue >= 0.15) {
                    terrainNoiseValue *= terrainNoiseValue * 6
                }
                 */

                val temperature = normalize(temperatureNoise.noise(blockX / 128.0, blockZ / 128.0), -1.0, 1.0)
                val height = ((terrainNoiseValue * 6) + SEA_LEVEL + 2).toInt()

                for (y in 0..height) {
                    /*
                    if (y == height) {
                        val floor = if (temperature > 0.65) SAND else GRASS_BLOCK
                        chunk.setBlockRaw(x, y, z, floor.defaultState)
                    } else if (y >= height - 3) {
                        val floor = if (temperature > 0.65) SANDSTONE else DIRT
                        chunk.setBlockRaw(x, y, z, floor.defaultState)
                    } else {
                        chunk.setBlockRaw(x, y, z, STONE.defaultState)
                    }*/

                    /* base terrain */
                    val result = normalize(noise.noise(blockX / 12.0, y / 24.0, blockZ / 12.0), -1.0, 1.0)

                    if (result <= 0.25) {
                        chunk.setBlockRaw(x, y, z, AIR.defaultState)
                    } else {
                        chunk.setBlockRaw(x, y, z, STONE.defaultState)
                    }
                }

                /* populate in water */
                for (y in SEA_LEVEL downTo 1) {
                    val block = chunk.getBlockRaw(x, y, z).block
                    if (block != AIR) {
                        break // stop filling when we hit our first floor block
                    }

                    chunk.setBlockRaw(x, y, z, WATER.defaultState)
                }

                /* painting terrain */
                val maxHeight = getHighestYLevel(chunk, x, z, checkViability = false)
                    ?: continue

                for (y in 127 downTo 1) {
                    val block = chunk.getBlockRaw(x, y, z).block
                    if (block == AIR || block == WATER || block == FLOWING_WATER) { // todo; ignore more non-solids
                        continue
                    }

                    if (y == maxHeight) {
                        val floor = if (temperature > 0.65) SAND else GRASS_BLOCK
                        chunk.setBlockRaw(x, y, z, floor.defaultState)
                    } else if (y >= maxHeight - 3) {
                        val floor = if (temperature > 0.65) SANDSTONE else DIRT
                        chunk.setBlockRaw(x, y, z, floor.defaultState)
                    } else {
                        chunk.setBlockRaw(x, y, z, STONE.defaultState)
                    }

                    /* fill blocks underneath water */
                    /* todo;
                    val floor = chunk.getBlockRaw(x, y - 1, z).block
                    if (floor != AIR) {
                        val type = when (floor) {
                            GRASS_BLOCK -> DIRT
                            SAND -> break
                            else -> GRAVEL
                        }

                        chunk.setBlockRaw(x, y - 1, z, type.defaultState)
                        break
                    }
                     */
                }

                placeFeatures(chunk, x, z, ThreadLocalRandom.current(), temperature)
            }
        }

        /*
//                 val maxHeight = normalize(noise.noise(blockX / 64.0, blockZ / 64.0), -1.0, 1.0)
        val noiseHeight = normalize(noise.noise(blockX / 32.0, blockZ / 32.0), -1.0, 1.0)

        // move origin value to 0 (from -1) and adjust up to sea level
        val heightY = ((noiseHeight * (/* maxHeight **/  32)) + 54).toInt()
         */

        return chunk
    }

    private fun placeFeatures(chunk: Chunk, blockX: Int, blockZ: Int, random: Random, temperature: Double) {
        val blockY = getHighestYLevel(chunk, blockX, blockZ)
            ?: return

        /* desert cacti */
        if (temperature >= 0.65) {
            if (random.nextDouble() <= 0.001) {
                for (y in 0 until (1..3).random()) {
                    chunk.setBlockRaw(blockX, blockY + y + 1, blockZ, CACTUS.defaultState)
                }

                // up to one per chunk
                return
            }
        } else {
            /* grass and (sometimes) flowers */
            if (random.nextDouble() < 0.01) {
                chunk.setBlockRaw(blockX, blockY + 1, blockZ, DANDELION.defaultState)
            } else if (random.nextDouble() < 0.25) {
                chunk.setBlockRaw(blockX, blockY + 1, blockZ, BlockState(SHORT_GRASS, 1))
            }
        }
    }

    private fun getHighestYLevel(chunk: Chunk, blockX: Int, blockZ: Int, checkViability: Boolean = true): Int? {
        for (y in 127 downTo 0) {
            val block = chunk.getBlockRaw(blockX, y, blockZ).block
            if (!checkViability && block != AIR) {
                return y
            }

            if (block == WATER || block == FLOWING_WATER) { // todo; check for other non-solids
                return null
            }

            if (block != AIR && block != WATER && block != FLOWING_WATER) { // todo; check for other non-solids
                return y
            }
        }

        return null
    }

    private fun normalize(value: Double, min: Double, max: Double): Double {
        return 1 - (value - min) / (max - min)
    }

    private companion object {
        const val SEA_LEVEL = 62
    }
}
