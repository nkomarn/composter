package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

data class ClientboundSetTimePacket(val time: Long) : Packet {
    companion object : PacketSerializer<ClientboundSetTimePacket> {
        override fun serialize(packet: ClientboundSetTimePacket, buffer: WriteBuffer) {
            buffer.writeLong(packet.time)
        }
    }
}