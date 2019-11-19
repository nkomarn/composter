package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.type.Location;

public class PacketPlayerPosition extends Packet {
    @Override
    public void handle(Session session, ByteBuf data) {
        if (session.getState() != State.PLAY) return;
        final double x = data.readDouble();
        final double y = data.readDouble();
        final double stance = data.readDouble();
        final double z = data.readDouble();
        final boolean onGround = data.readBoolean();
        session.getPlayer().setLocation(new Location(x, y, z));
    }
}
