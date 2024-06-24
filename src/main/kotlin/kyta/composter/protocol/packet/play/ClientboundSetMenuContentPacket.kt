package kyta.composter.protocol.packet.play

import kyta.composter.container.menu.Menu
import kyta.composter.item.isEmpty
import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer

data class ClientboundSetContainerContentPacket(
    val menuId: Int,
    val menu: Menu,
) : Packet {
    companion object : PacketSerializer<ClientboundSetContainerContentPacket> {
        override fun serialize(packet: ClientboundSetContainerContentPacket, buffer: WriteBuffer) {
            buffer.writeByte(packet.menuId)

            val slots = packet.menu.slots
            buffer.writeShort(slots.size)

            for (slot in slots) {
                val stack = slot.item

                if (stack.isEmpty) {
                    buffer.writeShort(-1)
                    continue
                }

                buffer.writeShort(stack.item.networkId)
                buffer.writeByte(stack.count)
                buffer.writeShort(stack.metadataValue)
            }
        }
    }
}