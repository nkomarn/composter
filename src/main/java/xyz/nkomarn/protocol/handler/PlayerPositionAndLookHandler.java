package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketPlayerPositionAndLook;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;

public class PlayerPositionAndLookHandler extends PacketHandler<PacketPlayerPositionAndLook> {
    @Override
    public void handle(Session session, Player player, PacketPlayerPositionAndLook message) {
        if (session.getState() != State.PLAY) return;
        player.setLocation(new Location(message.getX(), message.getY(), message.getZ(),
            message.getPitch(), message.getYaw()));
    }
}
