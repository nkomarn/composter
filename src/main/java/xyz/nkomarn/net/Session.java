package xyz.nkomarn.net;

import io.netty.channel.Channel;
import xyz.nkomarn.protocol.Packet;

public class Session {

    private final Channel channel;
    private State state;

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
    public void send(Packet packet) {
        channel.write(packet);
    }
}
