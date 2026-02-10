package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket

class ServerboundEntityActionPacket(val id: Int, val action: Action) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleEntityAction(this)

    companion object : PacketSerializer<ServerboundEntityActionPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundEntityActionPacket {
            val id = buffer.readInt()
            val action = when (buffer.readByte().toInt()) {
                1 -> Action.START_CROUCHING
                2 -> Action.STOP_CROUCHING
                3 -> Action.LEAVE_BED

                else -> throw IllegalStateException()
            }

            return ServerboundEntityActionPacket(id, action)
        }
    }

    enum class Action {
        START_CROUCHING,
        STOP_CROUCHING,
        LEAVE_BED,
    }
}