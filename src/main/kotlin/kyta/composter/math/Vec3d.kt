package kyta.composter.math

data class Vec3d(val x: Double, val y: Double, val z: Double) {
    companion object {
        val ZERO = Vec3d(0.0, 0.0, 0.0)
    }
}
