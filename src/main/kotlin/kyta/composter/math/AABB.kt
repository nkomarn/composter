package kyta.composter.math

data class AABB(
    val minX: Double,
    val minY: Double,
    val minZ: Double,
    val maxX: Double,
    val maxY: Double,
    val maxZ: Double,
) {
    constructor(x: Double, y: Double, z: Double) : this(-x / 2, -y / 2, -z / 2, x / 2, y / 2, z / 2)

    fun overlaps(box: AABB): Boolean {
        return minX <= box.maxX &&
                maxX >= box.minX &&
                minY <= box.maxY &&
                maxY >= box.minY &&
                minZ <= box.maxZ &&
                maxZ >= box.minZ
    }

    companion object {
        val ZERO = AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0)
    }
}