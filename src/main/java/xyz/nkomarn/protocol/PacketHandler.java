package xyz.nkomarn.protocol;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.packet.bi.ChatBiPacket;
import xyz.nkomarn.protocol.packet.c2s.HandshakeC2SPacket;
import xyz.nkomarn.protocol.packet.c2s.LoginC2SPacket;
import xyz.nkomarn.protocol.packet.c2s.PlayerPosC2SPacket;
import xyz.nkomarn.protocol.packet.c2s.PlayerPosLookC2SPacket;
import xyz.nkomarn.protocol.packet.s2c.HandshakeS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.LoginS2CPacket;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.type.Player;

public class PacketHandler {

    private final Composter server;

    public PacketHandler(@NotNull Composter server) {
        this.server = server;
    }

    public void handle(@NotNull Session session, @NotNull Packet<?> packet) {
        // TODO this is temporary and i know it's fucked up - also please use a hashmap for this istg
        Session.State state = session.getState();

        switch (packet.getId()) {
            case 0x01: // Login
                LoginC2SPacket loginInPacket = (LoginC2SPacket) packet;
                System.out.println("Logging in with protocol version " + loginInPacket.getProtocol());

                if (state == Session.State.LOGIN) {
                    session.setState(Session.State.PLAY); // TODO figure out the best placement
                    session.sendPacket(new LoginS2CPacket(1298, 971768181197178410L, (byte) 0, (byte) 1));
                    session.setPlayer(new Player(session, loginInPacket.getUsername()));
                } else {
//                    session.disconnect("Already logged in.");
                }

                break;
            case 0x02: // Handshake
                HandshakeC2SPacket handshakeInPacket = (HandshakeC2SPacket) packet;
                System.out.println("Handshaking with username: " + handshakeInPacket.getUsername());

                if (state == Session.State.HANDSHAKE) {
                    session.sendPacket(new HandshakeS2CPacket("-"));
                    session.setState(Session.State.LOGIN);
                } else {
//                    session.disconnect("Already shook hands.");
                }

                break;
            case 0x03: // Chat message
                ChatBiPacket chatInPacket = (ChatBiPacket) packet;
                Composter.broadcastMessage(chatInPacket.getMessage());
                break; // TODO this is dumb, handle this correctly later lmao
            case 0x0B:
                PlayerPosC2SPacket posInPacket = (PlayerPosC2SPacket) packet;
                Location currentLocation1 = session.getPlayer().getLocation();
                Location newLocation1 = new Location(
                        Composter.SPAWN.getWorld(), // TODO temporary
                        posInPacket.getX(),
                        posInPacket.getY(),
                        posInPacket.getZ(),
                        0,
                        0
                );

                if (!session.getPlayer().getWorld().isChunkLoaded(newLocation1.getChunk())) {
                    // System.out.println("Chunk is not loaded.");
                    session.getPlayer().teleport(currentLocation1);
                } else {
                    session.getPlayer().setLocation(newLocation1);
                }
                break;
            case 0x0D: // Pos + look
                PlayerPosLookC2SPacket posLookInPacket = (PlayerPosLookC2SPacket) packet;
                Location currentLocation = session.getPlayer().getLocation();
                Location newLocation = new Location(
                        Composter.SPAWN.getWorld(), // TODO temporary
                        posLookInPacket.getX(),
                        posLookInPacket.getY(),
                        posLookInPacket.getZ(),
                        posLookInPacket.getYaw(),
                        posLookInPacket.getPitch()
                );

                if (!session.getPlayer().getWorld().isChunkLoaded(newLocation.getChunk())) {
                    // System.out.println("Chunk is not loaded.");
                    session.getPlayer().teleport(currentLocation);
                } else {
                    session.getPlayer().setLocation(newLocation);
                }
        }
    }
}
