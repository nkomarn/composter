package xyz.nkomarn.composter.entity

import kyta.composter.Tickable
import kyta.composter.entity.EntityType
import kyta.composter.entity.data.SynchronizedEntityData
import kyta.composter.math.Vec3d
import kyta.composter.world.BlockPos
import kyta.composter.world.GlobalPos
import kyta.composter.world.World
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

open class Entity(val world: World): Tickable {
    val id = ENTITY_ID_COUNTER.getAndIncrement()
    val type = EntityType.PIG
    val uuid: UUID = UUID.randomUUID()
    val synchronizedData = SynchronizedEntityData()

    var x = 0.0
    var y = 0.0
    var z = 0.0
    var pitch = 0F
    var yaw = 0F

    var pos: Vec3d
        get() = Vec3d(x, y, z)
        set(value) {
            x = value.x
            y = value.y
            z = value.z

//             println("entity #$id moved -> ($x, $y, $z)")
        }

    var isRemoved: Boolean = false
        private set

    fun getBlockPos(): BlockPos {
        return BlockPos(pos)
    }

    open fun getGlobalPos(): GlobalPos {
        return GlobalPos(world, getBlockPos())
    }

    fun teleport(pos: GlobalPos) {
        // todo; handle cross-world movement
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
