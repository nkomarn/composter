package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketLogin;
import xyz.nkomarn.type.Player;

public class LoginHandler extends PacketHandler<PacketLogin> {
    @Override
    public void handle(Session session, Player player, PacketLogin message) {
        final State state = session.getState();

        if (state == State.LOGIN) {
            session.setState(State.PLAY);
            session.sendPacket(new PacketLogin(0, message.getUsername(), 0, 0)); // TODO send real values
            session.setPlayer(new Player(session, message.getUsername()));
        } else {
            session.disconnect("Already logged in.");
        }
    }
}
