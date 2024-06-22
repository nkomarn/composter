package kyta.composter.protocol.packet

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket
import kyta.composter.protocol.WriteBuffer

data class GenericDisconnectPacket(val reason: String) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleDisconnect(this)

    companion object : PacketSerializer<GenericDisconnectPacket> {
        override fun serialize(packet: GenericDisconnectPacket, buffer: WriteBuffer) {
            buffer.writeString(packet.reason)
        }

        override fun deserialize(buffer: ReadBuffer): GenericDisconnectPacket {
            return GenericDisconnectPacket(buffer.readString())
        }
    }
}