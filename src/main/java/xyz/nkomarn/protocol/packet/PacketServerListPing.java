package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;

public class PacketServerListPing extends Packet {
    @Override
    public void handle(Session session, ByteBuf data) {
        final State state = session.getState();

        if (state != State.HANDSHAKE) return;
        session.disconnect("Composter - Beta 1.7.3§0§10"); // TODO composter.yml configurable
    }
}
