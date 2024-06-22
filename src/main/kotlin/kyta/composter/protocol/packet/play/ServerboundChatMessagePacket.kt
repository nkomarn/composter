package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class ServerboundChatMessagePacket(val message: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleChatMessage(this)

    companion object : PacketSerializer<ServerboundChatMessagePacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundChatMessagePacket {
            return ServerboundChatMessagePacket(buffer.readString())
        }
    }
}