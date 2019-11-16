package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.PacketHandler;

import java.io.IOException;
import java.util.List;

//TODO change from void to something else? ALSO maybe move from decoder to normal listener?
public class Decoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws IOException {
        int opcode = buffer.readUnsignedByte();
        //System.out.println("Packet received: " + String.format("0x%01X", opcode));

        Packet packet = PacketHandler.getPacket(opcode);
        if (packet == null) {
            //throw new IOException("Invalid packet: " + opcode);
            return;
        }
        packet.handle(SessionManager.getSession(context.channel()), buffer);
    }
}
