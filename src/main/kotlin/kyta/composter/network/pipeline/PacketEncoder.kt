package kyta.composter.network.pipeline

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageEncoder
import kyta.composter.network.Connection
import kyta.composter.protocol.FlowDirection
import kyta.composter.protocol.MinecraftPacketBuffer
import kyta.composter.protocol.Packet
import org.slf4j.Logger

class PacketEncoder(
    private val connection: Connection,
    private val logger: Logger,
) : MessageToMessageEncoder<Packet>() {
    override fun encode(
        context: ChannelHandlerContext,
        packet: Packet,
        output: MutableList<Any>,
    ) {
        val definitions = connection.state.definitions[FlowDirection.CLIENTBOUND] ?: return
        val id = definitions.getId(packet::class)
        val buffer = MinecraftPacketBuffer.create(id)

        definitions.getSerializer(id)
            ?.serialize(packet, buffer)
            ?: return logger.warn("packet #$id does not have a registered serializer for state ${connection.state}")

        output.add(buffer.buf)
        logger.debug("sent packet #$id (${packet::class.simpleName}) with state ${connection.state}")
    }
}