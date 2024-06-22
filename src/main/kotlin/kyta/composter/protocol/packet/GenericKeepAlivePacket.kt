package kyta.composter.protocol.packet

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

object GenericKeepAlivePacket : ServerboundPacket, PacketSerializer<GenericKeepAlivePacket> {
    override suspend fun handle(handler: PacketHandler) = handler.handleKeepAlive(this)
    override fun deserialize(buffer: ReadBuffer): GenericKeepAlivePacket {
        return this
    }
}