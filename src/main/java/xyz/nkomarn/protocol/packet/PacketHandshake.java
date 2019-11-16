package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

public class PacketHandshake extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        State state = session.getState();

        if (state.equals(State.HANDSHAKE)) {
            System.out.println("Handshaking");

            // Debug shit
            //session.disconnect("Hi there, " + BufferUtil.readString(buffer));
            //session.disconnect("§dexzi is gay");

            session.setState(State.LOGIN);
            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(0x02);
            BufferUtil.writeString(buf, "-");
            session.send(buf);
        } else {
            session.disconnect("Already shook hands.");
        }
    }
}
