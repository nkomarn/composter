package xyz.nkomarn.composter.entity.tracker

import kyta.composter.Tickable
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import xyz.nkomarn.composter.entity.Entity
import kotlin.math.abs
import kotlin.math.max

class TrackedEntity(
    val entity: Entity,
    private val listener: (packet: Packet) -> Unit,
): Tickable {
    private var lastPos = entity.pos
    private var lastPitch = entity.pitch
    private var lastYaw = entity.yaw

    override fun tick(currentTick: Long) {
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
        /*
        val deltaX = toAbsoluteInteger(entity.x - oldPos.x)
        val deltaY = toAbsoluteInteger(entity.y - oldPos.y)
        val deltaZ = toAbsoluteInteger(entity.z - oldPos.z)

        if (fullUpdate || distanceMoved > MAX_RELATIVE_MOVE) {
            return ClientboundTeleportEntityPacket(entity)
        }

        if (headMoved) {
            return if (distanceMoved == 0.0) {
                ClientboundEntityLookPacket(entity)
            } else {
                ClientboundMoveLookEntityPacket(entity, deltaX, deltaY, deltaZ)
            }
        }

        val zeroRelativeMovement = deltaX == 0 && deltaY == 0 && deltaZ == 0
        if (!zeroRelativeMovement) {
            return ClientboundMoveEntityPacket(entity, deltaX, deltaY, deltaZ)
        }
         */

        return null
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