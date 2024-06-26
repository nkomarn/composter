package kyta.composter.math

data class AABB(
    val minX: Double,
    val minY: Double,
    val minZ: Double,
    val maxX: Double,
    val maxY: Double,
    val maxZ: Double,
)

fun AABB.overlaps(other: AABB): Boolean {
    return minX <= other.maxX &&
            maxX >= other.minX &&
            minY <= other.maxY &&
            maxY >= other.minY &&
            minZ <= other.maxZ &&
            maxZ >= other.minZ
}

fun AABB.grow(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): AABB {
    return AABB(minX - x, minY - y, minZ - z, maxX + x, maxY + y, maxZ + z)
}