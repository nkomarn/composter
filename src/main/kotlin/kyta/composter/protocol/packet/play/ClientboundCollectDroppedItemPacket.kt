package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

data class ClientboundCollectDroppedItemPacket(
    val id: Int,
    val collector: Int,
) : Packet {
    companion object : PacketSerializer<ClientboundCollectDroppedItemPacket> {
        override fun serialize(packet: ClientboundCollectDroppedItemPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeInt(packet.collector)
        }
    }
}