package xyz.nkomarn.composter.network.protocol;

import kyta.composter.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.packet.c2s.LoginC2SPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.PlayerPosC2SPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.PlayerPosLookC2SPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.ServerboundChatPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.ServerboundPlayerActionPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.ServerboundPlayerLookPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.HandshakeS2CPacket;

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

    public void handle(@NotNull Connection connection, @NotNull Packet<?> packet) {
        Handler handler = State.valueOf(connection.state().name()).getHandlerMap().get(packet.getId());

        if (handler == null) {
            // TODO something idk maybe asdhjkhjsadkfhasdjfhdhjbdasfhjkasdhkfafasjfadskhfjhk :))))
            return;
        }

        handler.handle(connection, packet);
    }

    static {
        // Login
        register(0x01, State.LOGIN, (session, packet) -> {
            LoginC2SPacket loginPacket = (LoginC2SPacket) packet;
            Composter server = session.getServer();
            var state = session.state();

            if (loginPacket.getProtocol() != 14) {
                session.disconnect(server.getConfig().getString("messages.unsupported_protocol"));
                return;
            }

            if (state != ConnectionState.LOGIN) {
                session.disconnect(server.getConfig().getString("messages.already_logged_in"));
                return;
            }

            session.getServer().executeOnMain(() -> {
                session.getServer().getPlayerManager().onLogin(session, loginPacket.getUsername());
            });
        });

        // Handshake
        register(0x02, State.HANDSHAKING, (session, packet) -> {
            var state = session.state();

            if (state == ConnectionState.HANDSHAKING) {
                session.sendPacket(new HandshakeS2CPacket("-"));
                session.setState(ConnectionState.LOGIN);
            } else {
                session.disconnect(session.getServer().getConfig().getString("messages.already_handshook"));
            }
        });

        // Chat message
        register(0x03, State.PLAY, (session, packet) -> {
            ServerboundChatPacket chatPacket = (ServerboundChatPacket) packet;
            session.getPlayer().ifPresent(player -> session.getServer().getPlayerManager().onChat(player, chatPacket.rawMessage()));
        });

        // Player position
        register(0x0B, State.PLAY, (session, packet) -> {
            PlayerPosC2SPacket posPacket = (PlayerPosC2SPacket) packet;
            session.getPlayer().ifPresent(player -> {
                var currentPos = player.getPos();
                var newPos = new Vec3d(posPacket.getX(), posPacket.getY(), posPacket.getZ());

                /* todo; handle these values appropriately */
                var pitch = player.getPitch();
                var yaw = player.getYaw();

                session.getServer().getPlayerManager().onMove(player, currentPos, newPos, pitch, yaw);
            });
        });

        register(0x0C, State.PLAY, (session, packet) -> {
            var lookPacket = (ServerboundPlayerLookPacket) packet;
            session.getPlayer().ifPresent(player -> {
                var currentPos = player.getPos();

                session.getServer().executeOnMain(() -> {
                    session.getServer().getPlayerManager().onMove(player, currentPos, currentPos, lookPacket.getPitch(), lookPacket.getYaw());
                });
            });
        });

        // Player position and look
        register(0x0D, State.PLAY, (session, packet) -> {
            PlayerPosLookC2SPacket posLookPacket = (PlayerPosLookC2SPacket) packet;
            session.getPlayer().ifPresent(player -> {
                var currentPos = player.getPos();
                var newPos = new Vec3d(posLookPacket.getX(), posLookPacket.getY(), posLookPacket.getZ());

                session.getServer().executeOnMain(() -> {
                    session.getServer().getPlayerManager().onMove(player, currentPos, newPos, posLookPacket.getPitch(), posLookPacket.getYaw());
                });
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

        // Client disconnection
        register(0xFF, State.PLAY, (connection, packet) -> {
            System.out.println("disconnect packet");
            connection.close();
        });

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

        void handle(@NotNull Connection connection, @NotNull Packet<?> packet);
    }
}
