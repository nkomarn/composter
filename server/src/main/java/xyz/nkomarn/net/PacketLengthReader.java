package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import xyz.nkomarn.util.DataTypeUtils;

import java.util.List;

public class PacketLengthReader extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        buffer.markReaderIndex();
        int packetLength = DataTypeUtils.readVarInt(buffer);

        if (buffer.readableBytes() < packetLength) {
            buffer.resetReaderIndex();
        } else {
            out.add(buffer.readBytes(packetLength));
        }
    }
}
