package xyz.nkomarn.composter.network.protocol.packet.play

import io.netty.buffer.ByteBuf
import kyta.composter.world.BlockPos
import xyz.nkomarn.composter.network.protocol.Packet
import kotlin.properties.Delegates

class ServerboundDigPacket : Packet<ServerboundDigPacket> {
    var action by Delegates.notNull<Action>()
    var blockPos by Delegates.notNull<BlockPos>()
    var blockFace by Delegates.notNull<Int>()

    constructor()

    constructor(action: Action, blockPos: BlockPos, blockFace: Int) {
        this.action = action
        this.blockPos = blockPos
        this.blockFace = blockFace
    }

    override fun getId(): Int {
        return 0x0E
    }

    override fun decode(buffer: ByteBuf): ServerboundDigPacket {
        val action = when (buffer.readByte().toInt()) {
            0 -> Action.START
            2 -> Action.FINISH
            4 -> Action.DROP_ITEM

            else -> throw UnsupportedOperationException()
        }

        val blockPos = BlockPos(
            buffer.readInt(),
            buffer.readByte().toInt(),
            buffer.readInt(),
        )

        return ServerboundDigPacket(action, blockPos, buffer.readByte().toInt())
    }

    enum class Action {
        START,
        FINISH,
        DROP_ITEM,
    }
}