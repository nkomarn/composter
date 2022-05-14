package xyz.nkomarn.composter.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.network.protocol.ConnectionState;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.network.protocol.packet.s2c.DisconnectS2CPacket;
import xyz.nkomarn.composter.entity.Player;

import java.util.Optional;

public class Connection {

    public static final AttributeKey<Connection> SESSION_KEY = AttributeKey.valueOf("session");
    private final Composter server;
    private final Channel channel;
    private ConnectionState state;
    private Player player;

    public Connection(@NotNull Composter server, @NotNull Channel channel) {
        this.server = server;
        this.channel = channel;
        this.state = ConnectionState.HANDSHAKING;
    }

    public @NotNull Composter getServer() {
        return server;
    }

    public ConnectionState state() {
        return state;
    }

    public void setState(final ConnectionState connectionState) {
        this.state = connectionState;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public void sendPacket(@NotNull Packet<?> packet) {
        channel.writeAndFlush(packet); // TODO send
    }


    public void disconnect(final String message) {
        channel.writeAndFlush(new DisconnectS2CPacket(message)).addListener(ChannelFutureListener.CLOSE);
    }

    public void close() {
        channel.close();
    }

    public void handlePacket(@NotNull Packet<?> packet) {
        server.getNetworkManager().getHandler().handle(this, packet);
        /*PacketHandler<Packet> handler = HandlerHandler.getHandler((Class<Packet>) packet.getClass());
        if (handler != null) {
            handler.handle(this, this.player, packet);
        }*/
    }
}
