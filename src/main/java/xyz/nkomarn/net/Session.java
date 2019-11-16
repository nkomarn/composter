package xyz.nkomarn.net;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.util.CharsetUtil;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

import java.util.ArrayDeque;
import java.util.Queue;

public class Session {

    private final Channel channel;
    private State state;

    // Packet queue
    private Queue<Packet> packetQueue = new ArrayDeque<>();

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

    // TODO getter/setter for player object

    // Sends packet to client
    public void send(ByteBuf buffer) {
        channel.writeAndFlush(buffer);
    }

    public void disconnect(final String message) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(0xFF);
        BufferUtil.writeString(buffer, message);
        channel.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
    }

}
