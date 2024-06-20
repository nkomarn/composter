package kyta.composter.protocol.packet.login

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.world.dimension.DimensionType

data class ClientboundLoginPacket(
    val entityId: Int,
    val serverId: String,
    val seed: Long,
    val dimension: DimensionType,
) : Packet {
    companion object : PacketSerializer<ClientboundLoginPacket> {
        override fun serialize(packet: ClientboundLoginPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.entityId)
            buffer.writeString(packet.serverId)
            buffer.writeLong(packet.seed)
            buffer.writeByte(packet.dimension.id)
        }
    }
}