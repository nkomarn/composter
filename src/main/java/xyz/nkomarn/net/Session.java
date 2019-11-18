package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.type.Player;
import xyz.nkomarn.util.ByteBufUtil;

import java.util.ArrayDeque;
import java.util.Queue;

public class Session {

    private final Channel channel;
    private State state;
    private Player player;

    public Session(final Channel channel) {
        this.channel = channel;
        this.state = State.HANDSHAKE; // default state

        SessionManager.openSession(this);
    }

    public State getState() {
        return this.state;
    }

    public void setState(final State state) {
        this.state = state;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void attachPlayer(final String username) {
        player = new Player(this, username);
    }

    public Player getPlayer() {
        return this.player;
    }

    // Sends packet to client
    public void send(final ByteBuf buffer) {
        if (!channel.isActive()) return;
        channel.writeAndFlush(buffer);
    }

    public void sendMessage(final String message) {
        ByteBuf chatMessage = Unpooled.buffer();
        chatMessage.writeByte(0x03);
        ByteBufUtil.writeString(chatMessage, message);
        this.send(chatMessage);
    }

    public void disconnect(final String message) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(0xFF);
        ByteBufUtil.writeString(buffer, message);
        channel.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
    }

}
