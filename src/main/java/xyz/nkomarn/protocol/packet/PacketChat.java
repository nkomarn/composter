package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketChat extends Packet {
    @Override
    public void handle(Session session, ByteBuf data) {
        final String message = ByteBufUtil.readString(data); // TODO sanitize
        Composter.getWorld().broadcastMessage(String.format("<%s> %s",
            session.getPlayer().getUsername(), message));
    }
}
