package kyta.composter.protocol.packet.play

import kyta.composter.math.Vec3d
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

interface FlyingStatusPacket {
    val onGround: Boolean
}

interface PositionPacket : FlyingStatusPacket {
    val pos: Vec3d
    val stance: Double
}

interface RotationPacket : FlyingStatusPacket {
    val yaw: Float
    val pitch: Float
}

// --

data class ServerboundSetPlayerFlyingStatusPacket(
    override val onGround: Boolean
) : ServerboundPacket, FlyingStatusPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerFlyingStatus(this)

    companion object : PacketSerializer<ServerboundSetPlayerFlyingStatusPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundSetPlayerFlyingStatusPacket {
            return ServerboundSetPlayerFlyingStatusPacket(buffer.readBoolean())
        }
    }
}

data class ServerboundSetPlayerPositionPacket(
    override val pos: Vec3d,
    override val stance: Double,
    override val onGround: Boolean,
) : ServerboundPacket, PositionPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerPosition(this)

    companion object : PacketSerializer<ServerboundSetPlayerPositionPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundSetPlayerPositionPacket {
            val x = buffer.readDouble()
            val y = buffer.readDouble()
            val stance = buffer.readDouble()
            val z = buffer.readDouble()

            return ServerboundSetPlayerPositionPacket(
                Vec3d(x, y, z),
                stance,
                buffer.readBoolean(),
            )
        }
    }
}

data class ServerboundSetPlayerRotationPacket(
    override val yaw: Float,
    override val pitch : Float,
    override val onGround: Boolean,
) : ServerboundPacket, RotationPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerRotation(this)

    companion object : PacketSerializer<ServerboundSetPlayerRotationPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundSetPlayerRotationPacket {
            return ServerboundSetPlayerRotationPacket(
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readBoolean(),
            )
        }
    }
}

data class ServerboundSetAbsolutePlayerPositionPacket(
    override val pos: Vec3d,
    override val stance: Double,
    override val yaw: Float,
    override val pitch: Float,
    override val onGround: Boolean,
) : ServerboundPacket, PositionPacket, RotationPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleAbsolutePlayerPosition(this)

    companion object : PacketSerializer<ServerboundSetAbsolutePlayerPositionPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundSetAbsolutePlayerPositionPacket {
            val x = buffer.readDouble()
            val y = buffer.readDouble()
            val stance = buffer.readDouble()
            val z = buffer.readDouble()

            return ServerboundSetAbsolutePlayerPositionPacket(
                Vec3d(x, y, z),
                stance,
                buffer.readFloat(),
                buffer.readFloat(),
                buffer.readBoolean(),
            )
        }
    }
}
