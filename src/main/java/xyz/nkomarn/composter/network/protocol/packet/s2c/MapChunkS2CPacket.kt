package xyz.nkomarn.composter.network.protocol.packet.s2c

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import kyta.composter.world.chunk.Chunk
import kyta.composter.world.chunk.LightData.Companion.DEFAULT_LIGHT_VALUE
import xyz.nkomarn.composter.network.protocol.Packet
import java.util.zip.Deflater

class MapChunkS2CPacket : Packet<MapChunkS2CPacket?> {
    private var x = 0
    private var z = 0
    private var y: Short = 0
    private lateinit var data: ByteArray

    constructor()

    constructor(chunk: Chunk) {
        this.x = chunk.pos.x * 16
        this.y = 0.toShort()
        this.z = chunk.pos.z * 16
        this.data = serializeChunk(chunk)
    }

    override fun getId(): Int {
        return 0x33
    }

    override fun encode(): ByteBuf {
        val deflater = Deflater(Deflater.BEST_SPEED) // TODO maybe make this configurable??
        deflater.setInput(data)
        deflater.finish()

        val compressedData = ByteArray(81920)
        val compressedBytes = deflater.deflate(compressedData)

        check(compressedBytes != 0) { "Faulty chunk compression: not all chunk bytes were compressed." }

        deflater.end()
        return Unpooled.buffer()
            .writeInt(x)
            .writeShort(y.toInt())
            .writeInt(z)
            .writeByte(15)
            .writeByte(127)
            .writeByte(15)
            .writeInt(compressedBytes)
            .writeBytes(compressedData, 0, compressedBytes)
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
            val value1 = first ?: DEFAULT_LIGHT_VALUE
            val value2 = second ?: DEFAULT_LIGHT_VALUE
            output.add(((value2 shl 4) or value1).toByte())
        }

        return output.toByteArray()
    }
}
