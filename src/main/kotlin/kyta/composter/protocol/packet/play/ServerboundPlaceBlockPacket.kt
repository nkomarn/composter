package kyta.composter.protocol.packet.play

import kyta.composter.math.Vec3i
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

    fun isBlockPlacement(): Boolean {
        return blockPos != SPECIAL_CASE_POS
    }

    companion object : PacketSerializer<ServerboundPlaceBlockPacket> {
        private val SPECIAL_CASE_POS = BlockPos(-1, -1, -1)

        override fun deserialize(buffer: ReadBuffer): ServerboundPlaceBlockPacket {
            val pos = buffer.readBlockPos()
            val direction = when (buffer.readByte().toInt()) {
                0 -> BlockFace.BOTTOM
                1 -> BlockFace.TOP
                2 -> BlockFace.NORTH
                3 -> BlockFace.SOUTH
                4 -> BlockFace.EAST
                5 -> BlockFace.WEST

                /* special case where the item is being "used" rather than placed */
                else -> null
            }

            val blockId = buffer.readByte().toInt()
            val amount = if (blockId >= 0) buffer.readByte().toInt() else 0
            val metadata = if (blockId >= 0) buffer.readByte().toInt() else 0

            /* todo; for now just default to NORTH as the direction for special case */
            return ServerboundPlaceBlockPacket(pos, direction ?: BlockFace.NORTH, blockId, amount, metadata)
        }
    }

    enum class BlockFace(val offset: Vec3i) {
        NORTH(Vec3i(0, 0, -1)), // -z
        EAST(Vec3i(-1, 0, 0)), // -x
        SOUTH(Vec3i(0, 0, 1)), // +z
        WEST(Vec3i(1, 0, 0)), // +x
        TOP(Vec3i(0, 1, 0)), // +y
        BOTTOM(Vec3i(0, -1, 0)), // -y
    }
}