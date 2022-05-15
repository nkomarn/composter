package xyz.nkomarn.composter.network.protocol.handler;

import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.Connection;
import xyz.nkomarn.composter.network.protocol.ConnectionState;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundHandshakePacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundDisconnectPacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundHandshakePacket;
import xyz.nkomarn.composter.network.protocol.packet.serverbound.ServerboundLoginPacket;
import xyz.nkomarn.composter.server.MinecraftServer;

import static net.kyori.adventure.text.Component.text;
import static xyz.nkomarn.composter.VersionConstants.PROTOCOL_VERSION;

public class DefaultPacketHandler implements PacketHandler {

    private final MinecraftServer server;
    private final Connection connection;

    public DefaultPacketHandler(MinecraftServer server, Connection connection) {
        this.server = server;
        this.connection = connection;
    }

    @Override
    public void handleHandshake(ServerboundHandshakePacket packet) {
        if (connection.state() != ConnectionState.HANDSHAKING) {
            connection.disconnect(text("Already completed handshaking phase."));
            return;
        }

        connection.sendPacket(new ClientboundHandshakePacket("-"));
        connection.setState(ConnectionState.LOGIN);
    }

    @Override
    public void handleLogin(ServerboundLoginPacket packet) {
        if (packet.protocolVersion() != PROTOCOL_VERSION) {
            connection.disconnect(text("Unsupported Minecraft client version."));
            return;
        }

        if (connection.state() != ConnectionState.LOGIN) {
            connection.disconnect(text("You are already logged into this server."));
            return;
        }

        var playerList = server.playerList();
        var player = playerList.player(packet.username().toLowerCase());

        if (player != null) {
            connection.disconnect(text("You are already logged into this server."));
            return;
        }

        var newPlayer = new Player(connection, packet.username());
        connection.bindPlayer(newPlayer);
        playerList.playerJoined(newPlayer);
    }

    @Override
    public void handleDisconnect(ServerboundDisconnectPacket packet) {
        connection.close();
    }
}
