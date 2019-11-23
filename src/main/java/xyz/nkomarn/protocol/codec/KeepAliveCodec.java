package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketKeepAlive;

import java.io.IOException;

public class KeepAliveCodec extends Codec<PacketKeepAlive> {
    public KeepAliveCodec() {
        super(PacketKeepAlive.class, 0x00);
    }

    @Override
    public ByteBuf encode(PacketKeepAlive packet) throws IOException {
        return Unpooled.EMPTY_BUFFER;
    }

    @Override
    public PacketKeepAlive decode(ByteBuf buffer) throws IOException {
        return new PacketKeepAlive();
    }
}
