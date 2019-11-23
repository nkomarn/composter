package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketDisconnect;
import xyz.nkomarn.util.ByteBufUtil;

import java.io.IOException;

public class DisconnectCodec extends Codec<PacketDisconnect> {
    public DisconnectCodec() {
        super(PacketDisconnect.class, 0xFF);
    }

    @Override
    public ByteBuf encode(PacketDisconnect packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtil.writeString(buffer, packet.getMessage());
        return buffer;
    }

    @Override
    public PacketDisconnect decode(ByteBuf buffer) throws IOException {
        return new PacketDisconnect(ByteBufUtil.readString(buffer));
    }
}
