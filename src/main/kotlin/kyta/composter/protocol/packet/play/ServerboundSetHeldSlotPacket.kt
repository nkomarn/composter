package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

class ServerboundSetHeldSlotPacket(val slot: Int) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleHeldSlotChange(this)

    companion object : PacketSerializer<ServerboundSetHeldSlotPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundSetHeldSlotPacket {
            return ServerboundSetHeldSlotPacket(buffer.readShort().coerceIn(0, 8).toInt())
        }
    }
}