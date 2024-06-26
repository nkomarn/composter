package kyta.composter.server

interface Tickable {
    fun tick(currentTick: Long)
}