package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.PacketHandler;

import java.io.IOException;
import java.util.List;

//TODO change from void to something else?
public class Decoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws IOException {
        int opcode = buffer.readUnsignedByte();
        System.out.println("Packet received: " + opcode);

        Packet packet = PacketHandler.getPacket(opcode);
        if (packet == null) {
            throw new IOException("Invalid packet: " + opcode);
        }
        packet.decode(buffer);
    }
}
