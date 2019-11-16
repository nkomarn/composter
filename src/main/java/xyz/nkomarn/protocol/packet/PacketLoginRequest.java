package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

public class PacketLoginRequest extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        State state = session.getState();

        if (state.equals(State.LOGIN)) {
            System.out.println("Login start");
            int protocol = buffer.readInt();

            // Protocol check
            if (protocol != 14) {
                session.disconnect("Invalid protocol version '" + protocol + "'.");
            }
        } else {
            session.disconnect("Not ready for login.");
        }
    }
}

