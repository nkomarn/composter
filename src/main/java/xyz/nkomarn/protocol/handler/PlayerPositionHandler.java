package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketPlayerPosition;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;

public class PlayerPositionHandler extends PacketHandler<PacketPlayerPosition> {
    @Override
    public void handle(Session session, Player player, PacketPlayerPosition message) {
        if (session.getState() != State.PLAY) return;
        player.setLocation(new Location(player.getWorld(), message.getX(), message.getY(), message.getZ(),
            player.getLocation().getPitch(), player.getLocation().getYaw()));
    }
}
