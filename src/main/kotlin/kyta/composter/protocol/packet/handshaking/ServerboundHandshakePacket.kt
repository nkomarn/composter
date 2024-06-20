package kyta.composter.protocol.packet.handshaking

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class ServerboundHandshakePacket(val username: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleHandshake(this)

    companion object : PacketSerializer<ServerboundHandshakePacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundHandshakePacket {
            return ServerboundHandshakePacket(buffer.readString())
        }
    }
}