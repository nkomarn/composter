package kyta.composter.protocol.packet.handshaking

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

data class ClientboundHandshakePacket(val hash: String) : Packet {
    companion object : PacketSerializer<ClientboundHandshakePacket> {
        override fun serialize(packet: ClientboundHandshakePacket, buffer: WriteBuffer) {
            buffer.writeString(packet.hash)
        }
    }
}