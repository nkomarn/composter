package xyz.nkomarn.protocol.scaffold;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.PacketKeepAlive;

import java.io.IOException;

public class PacketKeepAliveScaffold extends PacketScaffold<PacketKeepAlive> {

    private static final PacketKeepAlive KEEP_ALIVE = new PacketKeepAlive();

    public PacketKeepAliveScaffold() {
        super(PacketKeepAlive.class, 0x00);
    }

    @Override
    public ByteBuf encode(PacketKeepAlive message) throws IOException {
        return Unpooled.buffer();
    }

    @Override
    public PacketKeepAlive decode(ByteBuf buffer) throws IOException {
        return KEEP_ALIVE;
    }

}
