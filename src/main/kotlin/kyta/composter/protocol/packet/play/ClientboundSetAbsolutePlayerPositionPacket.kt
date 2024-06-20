package kyta.composter.protocol.packet.play

import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

data class ClientboundSetAbsolutePlayerPositionPacket(
    val pos: Vec3d,
    val stance: Double,
    val yaw: Float,
    val pitch: Float,
    val onGround: Boolean,
) : Packet {
    companion object : PacketSerializer<ClientboundSetAbsolutePlayerPositionPacket> {
        override fun serialize(packet: ClientboundSetAbsolutePlayerPositionPacket, buffer: WriteBuffer) {
            buffer.writeDouble(packet.pos.x)
            buffer.writeDouble(packet.pos.y)
            buffer.writeDouble(packet.stance)
            buffer.writeDouble(packet.pos.z)
            buffer.writeFloat(packet.yaw)
            buffer.writeFloat(packet.pitch)
            buffer.writeBoolean(packet.onGround)
        }
    }
}