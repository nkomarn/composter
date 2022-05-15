package xyz.nkomarn.composter.network;

import io.netty.channel.Channel;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.ConnectionState;
import xyz.nkomarn.composter.network.protocol.handler.DefaultPacketHandler;
import xyz.nkomarn.composter.network.protocol.handler.PacketHandler;
import xyz.nkomarn.composter.network.protocol.packet.Packet;
import xyz.nkomarn.composter.network.protocol.packet.clientbound.ClientboundDisconnectPacket;
import xyz.nkomarn.composter.server.MinecraftServer;

public class Connection {

    private final MinecraftServer server;
    private final Channel channel;
    private final PacketHandler packetHandler;
    private ConnectionState state;
    private Player player;

    public Connection(MinecraftServer server, Channel channel) {
        this.server = server;
        this.channel = channel;
        this.packetHandler = new DefaultPacketHandler(server, this);
        this.state = ConnectionState.HANDSHAKING;
    }

    public ConnectionState state() {
        return state;
    }

    public PacketHandler packetHandler() {
        return packetHandler;
    }

    public void setState(ConnectionState state) {
        this.state = state;
    }

    @Nullable
    public Player player() {
        return player;
    }

    public void bindPlayer(Player player) {
        this.player = player;
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public void disconnect(Component reason) {
        sendPacket(new ClientboundDisconnectPacket(reason));
    }

    public void handleDisconnection() {
        if (player != null) {
            server.playerList().playerDisconnected(player);
        }
    }

    public void close() {
        channel.closeFuture();
    }
}
