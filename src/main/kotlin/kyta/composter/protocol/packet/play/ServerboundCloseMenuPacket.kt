package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class ServerboundCloseMenuPacket(val id: Int): ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleMenuClose(this)

    companion object : PacketSerializer<ServerboundCloseMenuPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundCloseMenuPacket {
            return ServerboundCloseMenuPacket(buffer.readByte().toInt())
        }
    }
}