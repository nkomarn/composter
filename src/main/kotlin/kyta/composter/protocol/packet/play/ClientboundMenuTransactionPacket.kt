package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

data class ClientboundMenuTransactionPacket(
    val menuId: Int,
    val stateId: Int,
    val successful: Boolean,
) : Packet {
    companion object : PacketSerializer<ClientboundMenuTransactionPacket> {
        override fun serialize(packet: ClientboundMenuTransactionPacket, buffer: WriteBuffer) {
            buffer.writeByte(packet.menuId)
            buffer.writeShort(packet.stateId)
            buffer.writeBoolean(packet.successful)
        }
    }
}