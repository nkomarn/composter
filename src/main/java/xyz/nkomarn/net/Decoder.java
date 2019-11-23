package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.CodecHandler;

import java.io.IOException;
import java.util.List;

public class Decoder extends ReplayingDecoder<Packet> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws IOException {
        final int id = buffer.readUnsignedByte();

        System.out.println(String.format("Packet received: %s", id));

        final Codec codec = CodecHandler.getCodec(id);
        if (codec == null) {
            System.out.println("No codec.");
            //throw new IOException("Invalid packet: " + opcode);
            return;
        }
        list.add(codec.decode(buffer));
    }
}
