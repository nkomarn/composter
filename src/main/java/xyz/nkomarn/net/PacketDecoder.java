package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

//TODO change from void to something else?
public class PacketDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws Exception {
        int packetID = buffer.readUnsignedByte();

        System.out.println("Received packet: " + packetID);

        // TODO packet handling stuff here
    }
}
