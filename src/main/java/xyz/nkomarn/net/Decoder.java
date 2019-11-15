package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

//TODO change from void to something else?
public class Decoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) {
        int opcode = buffer.readUnsignedByte();
        System.out.println("Packet received: " + opcode);

        
    }
}
