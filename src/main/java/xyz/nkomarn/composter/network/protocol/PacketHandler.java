package xyz.nkomarn.composter.network.protocol;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.network.ConnectionState;
import xyz.nkomarn.composter.network.Session;
import xyz.nkomarn.composter.network.protocol.packet.bi.BidirectionalChatPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.LoginC2SPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.PlayerPosC2SPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.PlayerPosLookC2SPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.ServerboundPlayerActionPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.HandshakeS2CPacket;
import xyz.nkomarn.composter.type.Location;

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
        Handler handler = State.valueOf(session.connectionState().name()).getHandlerMap().get(packet.getId());

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
            var state = session.connectionState();

            if (loginPacket.getProtocol() != 14) {
                session.disconnect(server.getConfig().getString("messages.unsupported_protocol"));
                return;
            }

            if (state != ConnectionState.LOGIN) {
                session.disconnect(server.getConfig().getString("messages.already_logged_in"));
                return;
            }

            session.getServer().getPlayerManager().onLogin(session, loginPacket.getUsername());
        });

        // Handshake
        register(0x02, State.HANDSHAKING, (session, packet) -> {
            var state = session.connectionState();

            if (state == ConnectionState.HANDSHAKING) {
                session.sendPacket(new HandshakeS2CPacket("-"));
                session.setState(ConnectionState.LOGIN);
            } else {
                session.disconnect(session.getServer().getConfig().getString("messages.already_handshook"));
            }
        });

        // Chat message
        register(0x03, State.PLAY, (session, packet) -> {
            BidirectionalChatPacket chatPacket = (BidirectionalChatPacket) packet;
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

        register(0x13, State.PLAY, ((session, packet) -> {
            var actionPacket = (ServerboundPlayerActionPacket) packet;
            var player = session.getPlayer().orElseThrow();

            switch (actionPacket.getAction()) {
                case START_CROUCHING -> {
                    player.setCrouching(true);
                }

                case STOP_CROUCHING -> {
                    player.setCrouching(false);
                }
            }
        }));

        // Server list ping (for beta 1.8 - release 1.6.4)
        register(0xFE, State.HANDSHAKING, (session, packet) -> {
            Composter server = session.getServer();
            if (server.getConfig().getBoolean("motd.enabled")) {
                session.disconnect(String.format("%s§%s§%s", server.getConfig().getString("motd.message"),
                        server.getPlayerManager().getPlayers().size(), server.getConfig().getInteger("server.slots")
                ));
            }
        });
    }

    public enum State {

        HANDSHAKING(new HashMap<>()),
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
