package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.type.Location;

public class PacketPlayerPosition extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        final double x = buffer.readDouble();
        final double stance = buffer.readDouble();
        final double y = buffer.readDouble();
        final double z = buffer.readDouble();
        final float yaw = buffer.readFloat();
        final float pitch = buffer.readFloat();
        // TODO possibly use on ground boolean
        final Location location = new Location(x, y, z, pitch, yaw);
        session.getPlayer().setLocation(location);
    }
}
