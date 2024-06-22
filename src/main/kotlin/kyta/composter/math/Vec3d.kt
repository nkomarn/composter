package kyta.composter.math

import kyta.composter.world.BlockPos

interface Vec3<T> {
    val x: T
    val y: T
    val z: T
}

data class Vec3d(
    override val x: Double,
    override val y: Double,
    override val z: Double,
): Vec3<Double> {
    constructor(pos: BlockPos) : this(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

    fun add(x: Double, y: Double, z: Double): Vec3d {
        return Vec3d(this.x + x, this.y + y, this.z + z)
    }

    fun distanceSqrt(pos: Vec3d): Double {
        return (x - pos.x) * (x - pos.x) + (y - pos.y) * (y - pos.y) + (z - pos.z) * (z - pos.z)
    }

    companion object {
        val ZERO = Vec3d(0.0, 0.0, 0.0)
    }
}
