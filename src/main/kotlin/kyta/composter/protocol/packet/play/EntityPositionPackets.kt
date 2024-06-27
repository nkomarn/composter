package kyta.composter.protocol.packet.play

import kyta.composter.protocol.asAbsoluteInt
import kyta.composter.protocol.asRotation
import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

interface EntityRelativePositionPacket : Packet {
    val deltaX: Double
    val deltaY: Double
    val deltaZ: Double
}

interface EntityRotationPacket : Packet {
    val yaw: Float
    val pitch: Float
}

data class ClientboundSetEntityRotationPacket(
    private val id: Int,
    override val yaw: Float,
    override val pitch: Float,
): EntityRotationPacket {
    companion object : PacketSerializer<ClientboundSetEntityRotationPacket> {
        override fun serialize(packet: ClientboundSetEntityRotationPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
        }
    }
}

data class ClientboundAdjustEntityPositionPacket(
    val id: Int,
    override val deltaX: Double,
    override val deltaY: Double,
    override val deltaZ: Double,
) : EntityRelativePositionPacket {
    companion object : PacketSerializer<ClientboundAdjustEntityPositionPacket> {
        override fun serialize(packet: ClientboundAdjustEntityPositionPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.deltaX.asAbsoluteInt())
            buffer.writeByte(packet.deltaY.asAbsoluteInt())
            buffer.writeByte(packet.deltaZ.asAbsoluteInt())
        }
    }
}

data class ClientboundAdjustEntityPositionRotationPacket(
    val id: Int,
    override val deltaX: Double,
    override val deltaY: Double,
    override val deltaZ: Double,
    override val yaw: Float,
    override val pitch: Float,
) : EntityRelativePositionPacket, EntityRotationPacket {
    companion object : PacketSerializer<ClientboundAdjustEntityPositionRotationPacket> {
        override fun serialize(packet: ClientboundAdjustEntityPositionRotationPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.deltaX.asAbsoluteInt())
            buffer.writeByte(packet.deltaY.asAbsoluteInt())
            buffer.writeByte(packet.deltaZ.asAbsoluteInt())
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
        }
    }
}