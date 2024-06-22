package xyz.nkomarn.composter.entity

import kyta.composter.Tickable
import kyta.composter.entity.EntityType
import kyta.composter.entity.data.SynchronizedEntityData
import kyta.composter.math.AABB
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAddEntityPacket
import kyta.composter.world.BlockPos
import kyta.composter.world.GlobalPos
import kyta.composter.world.World
import kyta.composter.world.block.AIR
import kyta.composter.world.block.SHORT_GRASS
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

open class Entity(
    val world: World,
    val type: EntityType,
): Tickable {
    val id = ENTITY_ID_COUNTER.getAndIncrement()
    val uuid: UUID = UUID.randomUUID()
    val synchronizedData = SynchronizedEntityData()
    open val dimensions = 0.0 to 0.0

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

    fun getBoundingBox(): AABB {
        val pos = pos
        return AABB(
            pos.x - dimensions.first,
            pos.y,
            pos.z - dimensions.first,
            pos.x + dimensions.first,
            pos.y + dimensions.second,
            pos.z + dimensions.first,
        )
    }

    fun getCollidingEntities(): Sequence<Entity> {
        return world.entities.asSequence()
            .filterNot { it == this }
            .filter { getBoundingBox().overlaps(it.getBoundingBox()) }
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


    open fun createAddEntityPacket(): Packet {
        return ClientboundAddEntityPacket(this)
    }

    override fun tick(currentTick: Long) {
        /* gravity */
        if (this !is Player && world.getBlock(getBlockPos()).block == AIR && world.getBlock(getBlockPos()).block != SHORT_GRASS) {
            pos = pos.add(0.0, -0.25, 0.0)
        }
    }

    companion object {
        private val ENTITY_ID_COUNTER = AtomicInteger()
    }
}
