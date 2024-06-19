package xyz.nkomarn.composter.network.protocol.packet.play

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kyta.composter.world.BlockPos
import kyta.composter.world.block.BlockState
import xyz.nkomarn.composter.network.protocol.Packet
import kotlin.properties.Delegates

class ClientboundUpdateBlockPacket : Packet<ClientboundUpdateBlockPacket> {
    var blockPos by Delegates.notNull<BlockPos>()
    var blockState by Delegates.notNull<BlockState>()

    constructor()

    constructor(blockPos: BlockPos, blockState: BlockState) {
        this.blockPos = blockPos
        this.blockState = blockState
    }

    override fun getId(): Int {
        return 0x35
    }

    override fun encode(): ByteBuf {
        return Unpooled.buffer()
            .writeInt(blockPos.x)
            .writeByte(blockPos.y)
            .writeInt(blockPos.z)
            .writeByte(blockState.block.id)
            .writeByte(blockState.metadataValue)
    }
}