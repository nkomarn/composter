package kyta.composter.world

fun asCompactChunkKey(x: Int, z: Int): Long {
    return x.toLong() and 4294967295L or ((z.toLong() and 4294967295L) shl 32)
}

data class ChunkPos(val x: Int, val z: Int) {
    val compact by lazy { asCompactChunkKey(x, z) }
}
