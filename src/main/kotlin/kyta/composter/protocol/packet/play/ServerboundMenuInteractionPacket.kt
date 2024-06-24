package kyta.composter.protocol.packet.play

import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

data class ServerboundMenuInteractionPacket(
    val windowId: Int,
    val slot: Int,
    val rightClicked: Boolean,
    val stateId: Int,
    val shiftHeld: Boolean,
    val item: ItemStack,
) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleMenuInteraction(this)

    companion object : PacketSerializer<ServerboundMenuInteractionPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundMenuInteractionPacket {
            val windowId = buffer.readByte().toInt()
            val slot = buffer.readShort().toInt()
            val rightClicked = buffer.readBoolean()
            val stateId = buffer.readShort().toInt()
            val shiftHeld = buffer.readBoolean()
            val itemId = buffer.readShort().toInt()
            val item = Item(itemId) // todo; read item instance from registry
            var count = 1
            var metadataValue = 0

            if (itemId != -1) {
                count = buffer.readByte().toInt()
                metadataValue = buffer.readShort().toInt()
            }

            return ServerboundMenuInteractionPacket(
                windowId,
                slot,
                rightClicked,
                stateId,
                shiftHeld,
                ItemStack(item, count, metadataValue),
            )
        }
    }
}