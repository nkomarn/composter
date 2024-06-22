package kyta.composter.network.pipeline

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.ReplayingDecoder
import kyta.composter.protocol.ServerboundPacket
import kyta.composter.network.Connection
import kyta.composter.protocol.FlowDirection
import kyta.composter.protocol.MinecraftPacketBuffer
import org.slf4j.Logger

class PacketDecoder(
    private val connection: Connection,
    private val logger: Logger,
) : ReplayingDecoder<ServerboundPacket>() {
    override fun decode(context: ChannelHandlerContext, buf: ByteBuf, output: MutableList<Any>) {
        val definitions = connection.state.definitions[FlowDirection.SERVERBOUND] ?: return
        val id = buf.readUnsignedByte().toInt()
        val packet = definitions.getSerializer(id)
            ?.deserialize(MinecraftPacketBuffer(buf))
            ?: return logger.warn("received packet #$id which does not have a registered serializer for state ${connection.state}, ignoring..")

        output.add(packet)
        logger.debug("received packet #$id (${packet::class.simpleName}) with state ${connection.state}")
    }
}