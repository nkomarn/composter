package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.Packet;

public class PacketPing extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        //session.send(buffer);
    }
}
