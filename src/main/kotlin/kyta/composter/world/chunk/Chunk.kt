package kyta.composter.world.chunk

import kyta.composter.world.BlockPos
import kyta.composter.world.ChunkPos
import kyta.composter.world.block.AIR
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.defaultState
import kyta.composter.world.chunk.Chunk.Companion.BLOCKS_PER_CHUNK
import kotlin.math.min

class Chunk(val pos: ChunkPos) {
    val states = arrayOfNulls<BlockState>(BLOCKS_PER_CHUNK)
    val light = LightData()

    fun getBlock(pos: BlockPos): BlockState {
        return getBlockRaw(pos.x and 15, min(127, pos.y), pos.z and 15)
    }

    fun getBlockRaw(x: Int, y: Int, z: Int): BlockState {
        return states[arrayIndex(x, y, z)] ?: AIR.defaultState
    }

    fun setBlock(pos: BlockPos, state: BlockState?) {
        setBlockRaw(pos.x and 15, min(127, pos.y), pos.z and 15, state)
    }

    fun setBlockRaw(x: Int, y: Int, z: Int, state: BlockState?) {
        states[arrayIndex(x, y, z)] = state ?: AIR.defaultState
    }

    companion object {
        const val BLOCKS_PER_CHUNK = 16 * 16 * 128
    }
}

class LightData {
    val sky: Array<Int?> = arrayOfNulls(BLOCKS_PER_CHUNK)
    val block: Array<Int?> = arrayOfNulls(BLOCKS_PER_CHUNK)

    fun getSkyLight(pos: BlockPos): Int {
        return DEFAULT_LIGHT_VALUE
        // return sky[pos.arrayIndex] ?: DEFAULT_LIGHT_VALUE
    }

    fun getBlockLight(pos: BlockPos): Int {
        return DEFAULT_LIGHT_VALUE
        // return block[pos.arrayIndex] ?: DEFAULT_LIGHT_VALUE
    }

    companion object {
        const val DEFAULT_LIGHT_VALUE = 14 // todo; default to zero once the lighting engine is implemented
    }
}

fun arrayIndex(x: Int, y: Int, z: Int): Int {
    return (min(16, x) * 16 + min(16, z)) * 128 + min(128, y)

    // return x and 15, y and 15, z and 15
}