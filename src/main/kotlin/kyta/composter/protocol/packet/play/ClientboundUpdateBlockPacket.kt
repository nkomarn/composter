package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.world.BlockPos
import kyta.composter.world.block.BlockState

data class ClientboundUpdateBlockPacket(val blockPos: BlockPos, val blockState: BlockState) : Packet {
    companion object : PacketSerializer<ClientboundUpdateBlockPacket> {
        override fun serialize(packet: ClientboundUpdateBlockPacket, buffer: WriteBuffer) {
            buffer.writeBlockPos(packet.blockPos)
            buffer.writeByte(packet.blockState.block.id)
            buffer.writeByte(packet.blockState.metadataValue)
        }
    }
}