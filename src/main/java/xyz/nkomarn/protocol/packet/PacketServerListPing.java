package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.Packet;

public class PacketServerListPing extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        System.out.println("Pinged.");
        session.disconnect("Oof§1§999");
    }
}
