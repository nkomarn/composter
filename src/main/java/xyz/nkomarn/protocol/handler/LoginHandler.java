package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.Composter;
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
            session.sendPacket(new PacketLogin(0, "", 0, 0)); // TODO send real values
            final Player sessionPlayer = new Player(session, message.getUsername());
            Composter.addPlayer(sessionPlayer);
            session.setPlayer(sessionPlayer);
        } else {
            session.disconnect("Already logged in.");
        }
    }
}
