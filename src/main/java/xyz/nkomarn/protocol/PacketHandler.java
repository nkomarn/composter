package xyz.nkomarn.protocol;

import xyz.nkomarn.net.Session;
import xyz.nkomarn.type.Player;

public abstract class PacketHandler<T extends Packet> {
    public abstract void handle(final Session session, final Player player, T message);
}
