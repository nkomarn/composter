package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketChat;
import xyz.nkomarn.type.Player;

public class ChatHandler extends PacketHandler<PacketChat> {
    @Override
    public void handle(Session session, Player player, PacketChat message) {
        final State state = session.getState();

        if (state == State.PLAY) {
            System.out.println(message.getMessage());
            Composter.broadcastMessage(message.getMessage()); // TODO sanitize & custom format
        } else {
            session.disconnect("Attempting to send chat message in non-play state.");
        }
    }
}
