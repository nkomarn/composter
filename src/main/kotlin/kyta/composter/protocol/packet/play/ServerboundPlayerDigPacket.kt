package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket
import kyta.composter.world.BlockPos

data class ServerboundPlayerDigPacket(
    val blockPos: BlockPos,
    val blockFace: Int,
    val action: Action,
) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerDig(this)

    companion object : PacketSerializer<ServerboundPlayerDigPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundPlayerDigPacket {
            val action = when (buffer.readByte().toInt()) {
                0 -> Action.START
                2 -> Action.FINISH
                4 -> Action.DROP_ITEM

                else -> throw UnsupportedOperationException()
            }

            return ServerboundPlayerDigPacket(buffer.readBlockPos(), buffer.readByte().toInt(), action)
        }
    }

    enum class Action {
        START,
        FINISH,
        DROP_ITEM,
    }
}
