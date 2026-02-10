package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket
import kyta.composter.protocol.WriteBuffer

data class GenericPlayerActionPacket(
    val id: Int,
    val action: Action,
) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handlePlayerAction(this)

    companion object : PacketSerializer<GenericPlayerActionPacket> {
        override fun serialize(packet: GenericPlayerActionPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.id)
            buffer.writeByte(packet.action.networkId)
        }

        override fun deserialize(buffer: ReadBuffer): GenericPlayerActionPacket {
            val id = buffer.readInt()
            val actionId = buffer.readByte().toInt()
            val action = Action.entries.find { it.networkId == actionId }
                ?: throw IllegalStateException()

            return GenericPlayerActionPacket(id, action)
        }
    }

    enum class Action(internal val networkId: Int) {
        STOP_ANIMATION(0),
        SWING_ARM(1),
        TAKE_DAMAGE(2),
        LEAVE_BED(3),
        @Deprecated("Unused in practice, see ServerboundEntityActionPacket.")
        START_CROUCHING(104),
        @Deprecated("Unused in practice, see ServerboundEntityActionPacket.")
        STOP_CROUCHING(105),
    }
}
