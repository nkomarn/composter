package kyta.composter.protocol.packet

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket
import kyta.composter.protocol.WriteBuffer

class GenericKeepAlivePacket : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleKeepAlive(this)

    companion object : PacketSerializer<GenericKeepAlivePacket> {
        override fun deserialize(buffer: ReadBuffer): GenericKeepAlivePacket {
            return GenericKeepAlivePacket()
        }
    }
}