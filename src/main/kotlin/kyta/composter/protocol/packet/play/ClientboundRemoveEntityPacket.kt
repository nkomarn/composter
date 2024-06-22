package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import xyz.nkomarn.composter.entity.Entity

data class ClientboundRemoveEntityPacket(val id: Int) : Packet {
    constructor(entity: Entity) : this(entity.id)

    companion object : PacketSerializer<ClientboundRemoveEntityPacket> {
        override fun serialize(packet: ClientboundRemoveEntityPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
        }
    }
}