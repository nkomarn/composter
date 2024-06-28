package kyta.composter.server.world.entity.tracker

import kyta.composter.server.Tickable
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.play.ClientboundAdjustEntityPositionPacket
import kyta.composter.protocol.packet.play.ClientboundAdjustEntityPositionRotationPacket
import kyta.composter.protocol.packet.play.ClientboundSetEntityRotationPacket
import kyta.composter.protocol.packet.play.ClientboundTeleportEntityPacket
import kyta.composter.world.entity.Entity
import kotlin.math.abs
import kotlin.math.max
import kyta.composter.protocol.packet.play.ClientboundSetEntityDataPacket

class TrackedEntity(
    val entity: Entity,
    private val listener: (packet: Packet) -> Unit,
): Tickable {
    private var lastPos = entity.pos
    private var lastPitch = entity.pitch
    private var lastYaw = entity.yaw

    override fun tick(currentTick: Long) {
        listener.invoke(ClientboundSetEntityDataPacket(entity.id, entity.synchronizedData)) // todo; temporary

        val distanceMoved = distanceMoved(entity.pos)
        val headMoved = entity.pitch != lastPitch || entity.yaw != lastYaw

        if (!headMoved && distanceMoved == 0.0) {
            return
        }

        createPosUpdatePacket(lastPos, distanceMoved, headMoved, currentTick % 300 == 0L)
            ?.let { listener.invoke(it) }

        /**
         * update last known values.
         */
        lastPos = entity.pos
        lastPitch = entity.pitch
        lastYaw = entity.yaw
    }

    private fun createPosUpdatePacket(oldPos: Vec3d, distanceMoved: Double, headMoved: Boolean, fullUpdate: Boolean): Packet? {
        val deltaX = entity.x - oldPos.x
        val deltaY = entity.y - oldPos.y
        val deltaZ = entity.z - oldPos.z

        if (fullUpdate || distanceMoved > MAX_RELATIVE_MOVE) {
            return ClientboundTeleportEntityPacket(entity.id, entity.pos, entity.yaw, entity.pitch)
        }

        if (headMoved) {
            return if (distanceMoved == 0.0) {
                ClientboundSetEntityRotationPacket(entity.id, entity.yaw, entity.pitch)
            } else {
                ClientboundAdjustEntityPositionRotationPacket(entity.id, deltaX, deltaY, deltaZ, entity.yaw, entity.pitch)
            }
        }

        val zeroRelativeMovement = isIrrelevantMovement(deltaX) && isIrrelevantMovement(deltaY) && isIrrelevantMovement(deltaZ)
        if (!zeroRelativeMovement) {
            return ClientboundAdjustEntityPositionPacket(entity.id, deltaX, deltaY, deltaZ)
        }

        return null
    }

    private fun isIrrelevantMovement(value: Double): Boolean {
        return false && abs(value) < 0.01
    }

    private fun distanceMoved(currentPos: Vec3d): Double {
        val deltaX = abs(lastPos.x - currentPos.x)
        val deltaY = abs(lastPos.y - currentPos.y)
        val deltaZ = abs(lastPos.z - currentPos.z)
        return max(deltaX, max(deltaY, deltaZ))
    }

    private companion object {
        const val MAX_RELATIVE_MOVE = 4.0
    }
}