package xyz.nkomarn.net;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.entity.Player;

import java.util.Optional;

public class Session {

    public static final AttributeKey<Session> SESSION_KEY = AttributeKey.valueOf("session");
    private final Composter server;
    private final Channel channel;

    private ConnectionState state;
    private Player player;

    public Session(@NotNull Composter server, @NotNull Channel channel) {
        this.server = server;
        this.channel = channel;
        this.state = ConnectionState.HANDSHAKING;
    }

    public @NotNull Composter getServer() {
        return server;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public ConnectionState getState() {
        return state;
    }

    public void setState(ConnectionState state) {
        System.out.printf("Moved state from %s to %s.%n", this.state, state);
        this.state = state;
    }

    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public void sendPacket(@NotNull Packet<?> packet) {
        channel.writeAndFlush(packet);
    }

    /*public void queueIncomingPacket(final Packet packet) {
        queue.add(packet);
    }*/

    public void sendMessage(final String message) {
       // channel.writeAndFlush(new PacketChat(message));
    }

    public void disconnect(final String message) {
        //channel.writeAndFlush(new DisconnectS2CPacket(message)).addListener(ChannelFutureListener.CLOSE);
    }

    public void handlePacket(@NotNull Packet<?> packet) {
        server.getNetworkManager().getHandler().handle(this, packet);
        /*PacketHandler<Packet> handler = HandlerHandler.getHandler((Class<Packet>) packet.getClass());
        if (handler != null) {
            handler.handle(this, this.player, packet);
        }*/
    }

    /*public void tick() {
        Packet packet;
        while ((packet = queue.poll()) != null) { // TODO check cast
            PacketHandler<Packet> handler = (PacketHandler<Packet>) HandlerHandler.getHandler(packet.getClass());
            if (handler != null) {
                handler.handle(this, this.player, packet);
            }

            // TODO timeout
        }
    }*/

}
