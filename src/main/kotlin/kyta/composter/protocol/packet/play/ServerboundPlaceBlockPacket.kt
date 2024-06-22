package kyta.composter.protocol.packet.play

import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.ReadBuffer
import kyta.composter.protocol.ServerboundPacket
import kyta.composter.world.BlockPos

data class ServerboundPlaceBlockPacket(
    val blockPos: BlockPos,
    val face: BlockFace,
    val itemId: Int,
    val amount: Int,
    val metadata: Int,
) : ServerboundPacket {
    override suspend fun handle(handler: PacketHandler) = handler.handleBlockPlacement(this)

    companion object : PacketSerializer<ServerboundPlaceBlockPacket> {
        override fun deserialize(buffer: ReadBuffer): ServerboundPlaceBlockPacket {
            val pos = buffer.readBlockPos()
            val direction = when (buffer.readByte().toInt()) {
                0 -> BlockFace.BOTTOM
                1 -> BlockFace.TOP
                2 -> BlockFace.NORTH
                3 -> BlockFace.SOUTH
                4 -> BlockFace.EAST
                5 -> BlockFace.WEST

                else -> throw IllegalStateException()
            }

            val blockId = buffer.readByte().toInt()
            val amount = if (blockId != 0) buffer.readByte().toInt() else 0
            val metadata = if (blockId != 0) buffer.readByte().toInt() else 0
            return ServerboundPlaceBlockPacket(pos, direction, blockId, amount, metadata)
        }
    }

    enum class BlockFace {
        NORTH, // -z
        EAST, // -x
        SOUTH, // +z
        WEST, // +x
        TOP, // +y
        BOTTOM, // -y
    }
}