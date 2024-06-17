package kyta.composter.world

import xyz.nkomarn.composter.world.World

data class BlockPos(val x: Int, val y: Int, val z: Int)

data class ChunkPos(val x: Int, val z: Int) {
    constructor(pos: BlockPos) : this(pos.x shr 4, pos.z shr 4)
    val compact by lazy { asCompactChunkKey(x, z) }
}

class GlobalPos(val world: World, val pos: BlockPos)

fun asCompactChunkKey(x: Int, z: Int): Long {
    return x.toLong() and 4294967295L or ((z.toLong() and 4294967295L) shl 32)
}
