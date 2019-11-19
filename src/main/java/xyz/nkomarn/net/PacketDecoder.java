package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.PacketHandler;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ReplayingDecoder {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws IOException {
        int opcode = buffer.readUnsignedByte();
        System.out.println(String.format("Packet received: %s",
            opcode));

        Packet packet = PacketHandler.getPacket(opcode);
        if (packet == null) {
           //throw new IOException("Invalid packet: " + opcode);
            return;
        }
        packet.handle(SessionManager.getSession(context.channel()), buffer);
    }
}
