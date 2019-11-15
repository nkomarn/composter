package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.PacketManager;

import java.util.List;

//TODO change from void to something else?
public class Decoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws Exception {
        int opCode = buffer.readUnsignedByte();
        System.out.println("Packet received: " + opCode);

        Packet packet = PacketManager.lookup(opCode);

        /*PacketScaffold<?> scaffold = PacketLookup.find(packetID);
        if (scaffold == null) {
            //throw new IOException(String.format("Invalid packet ID: %s.", packetID));
        }

        System.out.println(scaffold);*/
        // TODO packet handling stuff here

    }
}
