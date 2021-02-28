package xyz.nkomarn.composter.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import xyz.nkomarn.composter.util.DataTypeUtils;

public class PacketLengthEncoder extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buffer, ByteBuf out) throws Exception {
        DataTypeUtils.writeVarInt(out, buffer.readableBytes());
        out.writeBytes(buffer);
    }
}
