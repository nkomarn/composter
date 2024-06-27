package kyta.composter.math

import kotlin.math.pow
import kyta.composter.world.BlockPos

interface Vec3<T : Number, U : Vec3<T, U>> {
    val x: T
    val y: T
    val z: T

    fun add(x: T, y: T, z: T): U
    fun subtract(x: T, y: T, z: T): U

    fun <T : Number, U: Vec3<T, U>> distanceSqRt(other: Vec3<T, U>): Double {
        return (x.toDouble() - other.x.toDouble()).pow(2) +
               (y.toDouble() - other.y.toDouble()).pow(2) +
               (z.toDouble() - other.z.toDouble()).pow(2)
    }
}

data class Vec3d(
    override val x: Double,
    override val y: Double,
    override val z: Double,
) : Vec3<Double, Vec3d> {
    constructor(pos: BlockPos) : this(pos.x.toDouble(), pos.y.toDouble(), pos.z.toDouble())

    override fun add(x: Double, y: Double, z: Double): Vec3d {
        return Vec3d(this.x + x, this.y + y, this.z + z)
    }

    override fun subtract(x: Double, y: Double, z: Double): Vec3d {
        return Vec3d(this.x - x, this.y - y, this.z - z)
    }

    companion object {
        val ZERO = Vec3d(0.0, 0.0, 0.0)
    }
}

data class Vec3i(
    override val x: Int,
    override val y: Int,
    override val z: Int,
) : Vec3<Int, Vec3i> {
    override fun add(x: Int, y: Int, z: Int): Vec3i {
        return Vec3i(this.x + x, this.y + y, this.z + z)
    }

    override fun subtract(x: Int, y: Int, z: Int): Vec3i {
        return Vec3i(this.x - x, this.y - y, this.z - z)
    }
}
