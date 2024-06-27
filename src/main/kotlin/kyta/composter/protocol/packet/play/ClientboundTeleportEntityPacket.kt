package kyta.composter.protocol.packet.play

import kyta.composter.protocol.asRotation
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

class ClientboundTeleportEntityPacket(
    val id: Int,
    val pos: Vec3d,
    val yaw: Float,
    val pitch: Float,
) : Packet {
    companion object : PacketSerializer<ClientboundTeleportEntityPacket> {
        override fun serialize(packet: ClientboundTeleportEntityPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeAbsoluteInt(packet.pos.x)
            buffer.writeAbsoluteInt(packet.pos.y)
            buffer.writeAbsoluteInt(packet.pos.z)
            buffer.writeByte(packet.yaw.asRotation())
            buffer.writeByte(packet.pitch.asRotation())
        }
    }
}