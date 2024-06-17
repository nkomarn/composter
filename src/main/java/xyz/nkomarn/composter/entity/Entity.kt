package xyz.nkomarn.composter.entity

import kyta.composter.Tickable
import xyz.nkomarn.composter.type.Location
import xyz.nkomarn.composter.world.World
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

open class Entity(val world: World): Tickable {
    val id = ENTITY_ID_COUNTER.getAndIncrement()
    val uuid = UUID.randomUUID()

    var x = 0
    var y = 0
    var z = 0

    @kotlin.jvm.JvmField
    protected var location: Location
    var isRemoved: Boolean = false
        private set

    init {
        this.id = ENTITY_ID_COUNTER.getAndIncrement()
        this.location = Location(world, 0.0, 15.0, 0.0)
        // TODO add entity to world entities
    }

    open fun getLocation(): Location? {
        return location
    }

    fun markRemoved() {
        this.isRemoved = true
    }

    override fun tick(currentTick: Long) {
    }

    companion object {
        private val ENTITY_ID_COUNTER = AtomicInteger()
    }
}
