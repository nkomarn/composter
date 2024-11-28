package kyta.composter.world

import kyta.composter.math.AABB
import kyta.composter.math.Vec3
import kyta.composter.math.Vec3d

data class BlockPos(
    override val x: Int,
    override val y: Int,
    override val z: Int,
) : Vec3<Int, BlockPos> {
    constructor(vector: Vec3d) : this(vector.x.toInt(), vector.y.toInt(), vector.z.toInt())
    val boundingBox by lazy { AABB(x + 0.0, y + 0.0, z + 0.0, x + 1.0, y + 1.0, z + 1.0) }

    override fun add(x: Int, y: Int, z: Int): BlockPos {
        return BlockPos(this.x + x, this.y + y, this.z + z)
    }

    override fun subtract(x: Int, y: Int, z: Int): BlockPos {
        return BlockPos(this.x - x, this.y - y, this.z - z)
    }
}

fun BlockPos.up(amount: Int = 1) = add(0, amount, 0)
fun BlockPos.down(amount: Int = 1) = add(0, -amount, 0)

data class ChunkPos(val x: Int, val z: Int) {
    constructor(pos: BlockPos) : this(pos.x shr 4, pos.z shr 4)

    val compact by lazy { asCompactChunkKey(x, z) }
}

class GlobalPos(val world: World, val pos: BlockPos)

fun asCompactChunkKey(x: Int, z: Int): Long {
    return x.toLong() and 4294967295L or ((z.toLong() and 4294967295L) shl 32)
}