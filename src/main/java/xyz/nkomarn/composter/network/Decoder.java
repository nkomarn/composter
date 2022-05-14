package xyz.nkomarn.composter.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.nkomarn.composter.network.protocol.Protocol;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger("Packet Decoder");
    private final Protocol protocol;

    public Decoder(@NotNull Protocol protocol) {
        this.protocol = protocol;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) {
        int bytes = buffer.readableBytes();

        if (bytes == 0) {
            return;
        }

        short packetId = buffer.readUnsignedByte();
        var hex = Integer.toHexString(packetId & 0xffff).toUpperCase();
        var session = context.channel().attr(Session.SESSION_KEY).get();
        var packet = session.connectionState().getPacketById(Direction.SERVERBOUND, packetId);

        if (packet == null) {
            // LOGGER.warn("Invalid packet id {} (0x{}) received.", packetId, hex);
            return;
        }

        // LOGGER.warn("Decoding packet id {} (0x{}) received.", packetId, hex);
        list.add(packet.decode(buffer));
    }
}
