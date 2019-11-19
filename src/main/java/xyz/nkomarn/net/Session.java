package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
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

    public void keepAlive() {
        ByteBuf keepAlive = Unpooled.buffer();
        keepAlive.writeInt(0x00);
        this.write(keepAlive);
    }

    // Sends packet to client
    public void write(final ByteBuf buffer) {
        if (!channel.isActive()) return;
        channel.writeAndFlush(buffer);
    }

    public void sendMessage(final String message) {
        ByteBuf chatMessage = Unpooled.buffer();
        chatMessage.writeByte(0x03);
        ByteBufUtil.writeString(chatMessage, message);
        this.write(chatMessage);
    }

    public void disconnect(final String message) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(0xFF);
        ByteBufUtil.writeString(buffer, message);
        channel.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
    }

}
