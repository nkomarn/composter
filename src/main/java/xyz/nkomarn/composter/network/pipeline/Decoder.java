package xyz.nkomarn.composter.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.Direction;
import xyz.nkomarn.composter.network.protocol.Protocol;
import xyz.nkomarn.composter.network.protocol.packet.c2s.ServerboundDisconnectPacket;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger("Packet Decoder");

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) {
        int bytes = buffer.readableBytes();

        if (bytes == 0) {
            return;
        }

        short packetId = buffer.readUnsignedByte();
        var hex = Integer.toHexString(packetId & 0xffff).toUpperCase();
        var session = context.channel().attr(Connection.SESSION_KEY).get();
        var packet = session.state().getPacketById(Direction.SERVERBOUND, packetId);

        if (packet == null) {
            // LOGGER.warn("Invalid packet id {} (0x{}) received.", packetId, hex);
            return;
        }

        // LOGGER.warn("Decoding packet id {} (0x{}) received.", packetId, hex);
        if (packet instanceof ServerboundDisconnectPacket) {
            return; // TODO
        }

        list.add(packet.decode(buffer));
    }
}
