package kyta.composter.world.entity

import java.util.concurrent.atomic.AtomicInteger
import kyta.composter.math.AABB
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddEntityPacket
import kyta.composter.server.world.entity.data.SynchronizedEntityData
import kyta.composter.world.BlockPos
import kyta.composter.world.GlobalPos
import kyta.composter.world.World

open class Entity(val type: EntityType) {
    val id = ENTITY_ID_COUNTER.getAndIncrement()
    val synchronizedData = SynchronizedEntityData()
    lateinit var world: World

    var ticksAlive: Long = 0
        private set

    open val dimensions = 0.0 to 0.0

    var x = 0.0
    var y = 0.0
    var z = 0.0

    var pitch = 0F
        set(value) {
            field = value % 360F
        }

    var yaw = 0F
        set(value) {
            field = value % 360F
        }

    var isRemoved: Boolean = false
        private set

    fun markRemoved() {
        this.isRemoved = true
    }

    open fun createAddEntityPacket(): Packet {
        return ClientboundAddEntityPacket(this)
    }

    open fun tick(currentTick: Long, world: World) {
        this.world = world
        ticksAlive++
    }

    companion object {
        private val ENTITY_ID_COUNTER = AtomicInteger()
    }
}

val Entity.blockPos: BlockPos
    get() = BlockPos(pos)

val Entity.globalPos: GlobalPos
    get() = GlobalPos(world, blockPos)

val Entity.boundingBox: AABB
    get() = pos.let { pos ->
        AABB(
            pos.x - dimensions.first / 2.0,
            pos.y,
            pos.z - dimensions.first / 2.0,
            pos.x + dimensions.first / 2.0,
            pos.y + dimensions.second,
            pos.z + dimensions.first / 2.0,
        )
    }

var Entity.pos: Vec3d
    get() = Vec3d(x, y, z)
    set(value) {
        x = value.x
        y = value.y
        z = value.z
    }