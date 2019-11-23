package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketHandshake;
import xyz.nkomarn.util.ByteBufUtil;

import java.io.IOException;

public class HandshakeCodec extends Codec<PacketHandshake> {
    public HandshakeCodec() {
        super(PacketHandshake.class, 0x02);
    }

    @Override
    public ByteBuf encode(PacketHandshake packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        ByteBufUtil.writeString(buffer, packet.getUsername());
        return buffer; // TODO make an outbound handshake class, although not necessary
    }

    @Override
    public PacketHandshake decode(ByteBuf buffer) throws IOException {
        final String username = ByteBufUtil.readString(buffer);
        return new PacketHandshake(username);
    }
}
