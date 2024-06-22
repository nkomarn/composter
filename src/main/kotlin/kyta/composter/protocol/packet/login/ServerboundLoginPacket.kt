package kyta.composter.protocol.packet.login

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class ServerboundLoginPacket(val protocolVersion: Int, val username: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleLogin(this)

    companion object : PacketSerializer<ServerboundLoginPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundLoginPacket {
            return ServerboundLoginPacket(buffer.readInt(), buffer.readString())
        }
    }
}