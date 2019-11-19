package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketChat extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        final String message = ByteBufUtil.readString(buffer);
        Composter.getWorld().broadcastMessage(message);
        System.out.println(message);
    }
}
