package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.net.State;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketHandshake extends Packet {
    @Override
    public void handle(Session session, ByteBuf data) {
        final State state = session.getState();

        if (state == State.HANDSHAKE) {
            ByteBuf handshake = Unpooled.buffer();
            handshake.writeByte(0x02);
            ByteBufUtil.writeString(handshake, "-");
            session.write(handshake);
            session.setState(State.LOGIN);
        } /*else {
            session.disconnect("Already completed handshake.");
        }*/
    }
}
