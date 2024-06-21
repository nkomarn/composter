package kyta.composter.world

import kyta.composter.math.Vec3
import kyta.composter.math.Vec3d

data class BlockPos(
    override val x: Int,
    override val y: Int,
    override val z: Int,
) : Vec3<Int> {
    constructor(vector: Vec3d) : this(vector.x.toInt(), vector.y.toInt(), vector.z.toInt())

    fun up(amount: Int = 1) = add(0, amount, 0)
    fun down(amount: Int = 1) = add(0, -amount, 0)

    fun add(x: Int, y: Int, z: Int): BlockPos {
        return BlockPos(this.x + x, this.y + y, this.z + z)
    }

    fun distanceSqrt(pos: BlockPos): Int {
        return (x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y) + (z - pos.z) * (z - pos.z)
    }
}

data class ChunkPos(val x: Int, val z: Int) {
    constructor(pos: BlockPos) : this(pos.x shr 4, pos.z shr 4)

    val compact by lazy { asCompactChunkKey(x, z) }
}

class GlobalPos(val world: World, val pos: BlockPos)

fun asCompactChunkKey(x: Int, z: Int): Long {
    return x.toLong() and 4294967295L or ((z.toLong() and 4294967295L) shl 32)
}
