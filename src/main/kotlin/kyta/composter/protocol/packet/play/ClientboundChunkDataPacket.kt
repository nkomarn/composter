package kyta.composter.protocol.packet.play

import kyta.composter.protocol.Packet
import kyta.composter.protocol.PacketSerializer
import kyta.composter.protocol.WriteBuffer
import kyta.composter.world.ChunkPos
import kyta.composter.world.chunk.Chunk
import kyta.composter.world.chunk.LightData.Companion.DEFAULT_LIGHT_VALUE
import java.util.zip.Deflater

data class ClientboundChunkDataPacket(val pos: ChunkPos, val data: Chunk) : Packet {
    companion object : PacketSerializer<ClientboundChunkDataPacket> {
        override fun serialize(packet: ClientboundChunkDataPacket, buffer: WriteBuffer) {
            buffer.writeInt(packet.pos.x * 16)
            buffer.writeShort(0)
            buffer.writeInt(packet.pos.z * 16)
            buffer.writeByte(15)
            buffer.writeByte(127)
            buffer.writeByte(15)

            /* compress the chunk data before serializing */
            val serializedData = serializeChunk(packet.data)
            val deflater = Deflater(Deflater.DEFAULT_STRATEGY)

            deflater.setInput(serializedData)
            deflater.finish()

            val compressedData = ByteArray(81920)
            val compressedSize = deflater.deflate(compressedData)

            deflater.end()
            buffer.writeInt(compressedSize)
            buffer.writeBytes(compressedData, 0, compressedSize)
        }

        private fun serializeChunk(chunk: Chunk): ByteArray {
            val output = ArrayList<Byte>((Chunk.BLOCKS_PER_CHUNK * 5) / 2)

            chunk.states.forEach {
                output.add(it?.block?.id?.toByte() ?: 0)
            }

            // todo; this is a bit weird
            chunk.states.asSequence().chunked(2).forEach { (first, second) ->
                val value1 = first?.metadataValue ?: 0
                val value2 = second?.metadataValue ?: 0
                output.add(((value2 shl 4) or value1).toByte())
            }

            chunk.light.sky.asSequence().chunked(2).forEach { (first, second) ->
                val value1 = first ?: DEFAULT_LIGHT_VALUE
                val value2 = second ?: DEFAULT_LIGHT_VALUE
                output.add(((value2 shl 4) or value1).toByte())
            }

            chunk.light.block.asSequence().chunked(2).forEach { (first, second) ->
                val value1 = first ?: DEFAULT_LIGHT_VALUE - 1
                val value2 = second ?: DEFAULT_LIGHT_VALUE - 1
                output.add(((value2 shl 4) or value1).toByte())
            }

            return output.toByteArray()
        }
    }
}
