package xyz.nkomarn.protocol;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.packet.bi.ChatBiPacket;
import xyz.nkomarn.protocol.packet.c2s.LoginC2SPacket;
import xyz.nkomarn.protocol.packet.c2s.PlayerPosC2SPacket;
import xyz.nkomarn.protocol.packet.c2s.PlayerPosLookC2SPacket;
import xyz.nkomarn.protocol.packet.s2c.HandshakeS2CPacket;
import xyz.nkomarn.type.Location;

import java.util.HashMap;
import java.util.Map;

public class PacketHandler {

    private final Composter server;

    public PacketHandler(@NotNull Composter server) {
        this.server = server;
    }

    public static void register(int id, @NotNull State state, @NotNull Handler handler) {
        state.getHandlerMap().put(id, handler);
    }

    public void handle(@NotNull Session session, @NotNull Packet<?> packet) {
        Handler handler = State.valueOf(session.getState().name()).getHandlerMap().get(packet.getId());

        if (handler == null) {
            // TODO something idk maybe asdhjkhjsadkfhasdjfhdhjbdasfhjkasdhkfafasjfadskhfjhk :))))
            return;
        }

        handler.handle(session, packet);
    }

    static {
        // Login
        register(0x01, State.LOGIN, (session, packet) -> {
            LoginC2SPacket loginPacket = (LoginC2SPacket) packet;
            Composter server = session.getServer();
            Session.State state = session.getState();

            if (loginPacket.getProtocol() != 14) {
                session.disconnect(server.getConfig().getString("messages.unsupported_protocol"));
                return;
            }

            if (state != Session.State.LOGIN) {
                session.disconnect(server.getConfig().getString("messages.already_logged_in"));
                return;
            }

            session.getServer().getPlayerManager().onLogin(session, loginPacket.getUsername());
        });

        // Handshake
        register(0x02, State.HANDSHAKE, (session, packet) -> {
            Session.State state = session.getState();

            if (state == Session.State.HANDSHAKE) {
                session.sendPacket(new HandshakeS2CPacket("-"));
                session.setState(Session.State.LOGIN);
            } else {
                session.disconnect(session.getServer().getConfig().getString("messages.already_handshook"));
            }
        });

        // Chat message
        register(0x03, State.PLAY, (session, packet) -> {
            ChatBiPacket chatPacket = (ChatBiPacket) packet;
            session.getPlayer().ifPresent(player -> session.getServer().getPlayerManager().onChat(player, chatPacket.getMessage()));
        });

        // Player position
        register(0x0B, State.PLAY, (session, packet) -> {
            PlayerPosC2SPacket posPacket = (PlayerPosC2SPacket) packet;
            session.getPlayer().ifPresent(player -> {
                Location oldLocation = player.getLocation();
                Location newLocation = new Location(
                        oldLocation.getWorld(), // TODO use actual world
                        posPacket.getX(),
                        posPacket.getY(),
                        posPacket.getZ(),
                        oldLocation.getYaw(),
                        oldLocation.getPitch()
                );

                session.getServer().getPlayerManager().onMove(player, oldLocation, newLocation);
            });
        });

        // Player position and look
        register(0x0D, State.PLAY, (session, packet) -> {
            PlayerPosLookC2SPacket posLookPacket = (PlayerPosLookC2SPacket) packet;
            session.getPlayer().ifPresent(player -> {
                Location oldLocation = player.getLocation();
                Location newLocation = new Location(
                        oldLocation.getWorld(), // TODO use actual world
                        posLookPacket.getX(),
                        posLookPacket.getY(),
                        posLookPacket.getZ(),
                        posLookPacket.getYaw(),
                        posLookPacket.getPitch()
                );

                session.getServer().getPlayerManager().onMove(player, oldLocation, newLocation);
            });
        });

        // Server list ping (for beta 1.8 - release 1.6.4)
        register(0xFE, State.HANDSHAKE, (session, packet) -> {
            Composter server = session.getServer();
            if (server.getConfig().getBoolean("motd.enabled")) {
                session.disconnect(String.format("%s§%s§%s", server.getConfig().getString("motd.message"),
                        server.getPlayerManager().getPlayers().size(), server.getConfig().getInteger("server.slots")
                ));
            }
        });
    }

    public enum State {

        HANDSHAKE(new HashMap<>()),
        LOGIN(new HashMap<>()),
        PLAY(new HashMap<>());

        private final Map<Integer, Handler> map;

        State(@NotNull HashMap<Integer, Handler> map) {
            this.map = map;
        }

        Map<Integer, Handler> getHandlerMap() {
            return map;
        }
    }

    @FunctionalInterface
    public interface Handler {

        void handle(@NotNull Session session, @NotNull Packet<?> packet);
    }
}
