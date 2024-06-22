package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

data class ClientboundChatMessagePacket(val message: Component): Packet {
    companion object : PacketSerializer<ClientboundChatMessagePacket> {
        override fun serialize(packet: ClientboundChatMessagePacket, buffer: WriteBuffer) {
            buffer.writeString(LegacyComponentSerializer.legacySection().serialize(packet.message))
        }
    }
}