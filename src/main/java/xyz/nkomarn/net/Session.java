package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import xyz.nkomarn.protocol.HandlerHandler;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.protocol.PacketHandler;
import xyz.nkomarn.protocol.packets.PacketChat;
import xyz.nkomarn.protocol.packets.PacketDisconnect;
import xyz.nkomarn.protocol.packets.PacketKeepAlive;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.ByteBufUtil;

import java.util.ArrayDeque;
import java.util.Queue;

public class Session {

    private final Channel channel;
    private State state;
    private Player player;

    private final Queue<Packet> queue = new ArrayDeque<>();

    public Session(final Channel channel) {
        this.channel = channel;
        this.state = State.HANDSHAKE;
        SessionManager.openSession(this);
    }

    public Channel getChannel() {
        return this.channel;
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(final Player player) {
        this.player = player;
    }

    public void sendPacket(final Packet packet) {
        channel.writeAndFlush(packet); // TODO send
    }

    public void queueIncomingPacket(final Packet packet) {
        queue.add(packet);
    }

    public void sendMessage(final String message) {
        channel.writeAndFlush(new PacketChat(message));
    }

    public void disconnect(final String message) {
        channel.writeAndFlush(new PacketDisconnect(message))
            .addListener(ChannelFutureListener.CLOSE);
    }

    public void tick() {
        Packet packet;
        while ((packet = queue.poll()) != null) { // TODO check cast
            PacketHandler<Packet> handler = (PacketHandler<Packet>) HandlerHandler.getHandler(packet.getClass());
            if (handler != null) {
                handler.handle(this, this.player, packet);
            }

            // TODO timeout
        }
    }
}
