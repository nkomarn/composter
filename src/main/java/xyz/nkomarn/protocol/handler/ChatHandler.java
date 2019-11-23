package xyz.nkomarn.protocol.handler;

import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketChat;
import xyz.nkomarn.type.Player;

public class ChatHandler extends PacketHandler<PacketChat> {
    @Override
    public void handle(Session session, Player player, PacketChat message) {
        if (session.getState() != State.PLAY) return;
        session.getPlayer().getWorld().broadcastMessage(message.getMessage()); // TODO sanitize & custom format
    }
}
