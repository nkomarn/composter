package xyz.nkomarn.protocol.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.net.Session;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.ByteBufUtil;

public class PacketServerListPing extends Packet {
    @Override
    public void handle(Session session, ByteBuf buffer) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte(0xFF);
        ByteBufUtil.writeString(buf, "Composter - Beta 1.7.3§0§10"); // TODO configurable & session count
        session.write(buf);
    }
}
