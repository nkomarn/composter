package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketHandshake extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        State state = session.getState();

        if (state.equals(State.HANDSHAKE)) {
            session.setState(State.LOGIN);
            ByteBuf buf = Unpooled.buffer();
            buf.writeByte(0x02);
            ByteBufUtil.writeString(buf, "-");
            session.send(buf);
        } else {
            //session.disconnect("Already shook hands.");
        }
    }
}
