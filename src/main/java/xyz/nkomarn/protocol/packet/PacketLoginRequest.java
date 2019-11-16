package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketLoginRequest extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        State state = session.getState();
        
        if (state.equals(State.LOGIN)) {
            int protocol = buffer.readInt();

            // Protocol check
            if (protocol != 14) {
                session.disconnect("Unsupported protocol version '" + protocol + "'.");
                return;
            }

            ByteBuf buf = Unpooled.buffer();
            buf.writeInt(0x01);
            buf.writeInt(1298); // bullshit entity id
            ByteBufUtil.writeString(buf, "");
            buf.writeLong(971768181197178410L); // bullshit seed
            buf.writeInt(0);
            buf.writeByte(0);
            buf.writeByte(1);
            buf.writeByte(128);
            buf.writeByte(8);
            session.send(buf);
        } else {
            session.disconnect("Not ready for login.");
        }
    }
}

