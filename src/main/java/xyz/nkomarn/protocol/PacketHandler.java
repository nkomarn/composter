package xyz.nkomarn.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.net.ConnectionState;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.packet.c2s.HandshakeC2SPacket;
import xyz.nkomarn.protocol.packet.c2s.StatusPingC2SPacket;
import xyz.nkomarn.protocol.packet.s2c.StatusPongS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.StatusResponseS2CPacket;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class PacketHandler {

    private static final EnumMap<ConnectionState, Int2ObjectMap<Handler>> HANDLERS = new EnumMap<>(ConnectionState.class);
    private final Composter server;

    public PacketHandler(@NotNull Composter server) {
        this.server = server;
    }

    public static void register(int id, ConnectionState state, Handler handler) {
        HANDLERS.computeIfAbsent(state, state1 -> new Int2ObjectOpenHashMap<>()).put(id, handler);
    }

    public void handle(Session session, Packet<?> packet) {
        var handler = HANDLERS.get(session.getState()).get(packet.getId());
        if (handler == null) {
            // TODO something idk maybe asdhjkhjsadkfhasdjfhdhjbdasfhjkasdhkfafasjfadskhfjhk :))))
            return;
        }

        handler.handle(session, packet);
    }

    static {
        register(0x00, ConnectionState.HANDSHAKING, (session, packet) -> {
            var handshakePacket = (HandshakeC2SPacket) packet;
            session.setState(handshakePacket.getNextState());
        });

        register(0x01, ConnectionState.STATUS, (session, packet) -> {
            var pingPacket = (StatusPingC2SPacket) packet;
            session.sendPacket(new StatusPongS2CPacket(pingPacket.getTimestamp()));
        });

        register(0x00, ConnectionState.STATUS, (session, packet) -> {
            session.sendPacket(new StatusResponseS2CPacket()); // TODO edit MOTD field later
        });

        // Login
        /*register(0x01, State.LOGIN, (session, packet) -> {
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
        });*/
    }

    @FunctionalInterface
    public interface Handler {

        void handle(@NotNull Session session, @NotNull Packet<?> packet);
    }
}
