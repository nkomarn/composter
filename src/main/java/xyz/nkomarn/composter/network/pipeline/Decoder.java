package xyz.nkomarn.composter.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.Direction;

import java.util.List;

public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) {
        int bytes = buffer.readableBytes();

        if (bytes == 0) {
            return;
        }

        short packetId = buffer.readUnsignedByte();
//         var hex = Integer.toHexString(packetId & 0xffff).toUpperCase();
        var session = context.channel().attr(Connection.SESSION_KEY).get();
        var packet = session.state().getPacketById(Direction.SERVERBOUND, packetId);

        if (packet == null) {
            return;
        }

        list.add(packet.decode(buffer));
    }
}
