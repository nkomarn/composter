package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketHandshake;
import xyz.nkomarn.type.Player;

public class HandshakeHandler extends PacketHandler<PacketHandshake> {
    @Override
    public void handle(Session session, Player player, PacketHandshake message) {
        final State state = session.getState();

        if (state == State.HANDSHAKE) {
            session.setState(State.LOGIN);
            session.sendPacket(new PacketHandshake("-"));
        } else {
            session.disconnect("Already shook hands.");
        }
    }
}
