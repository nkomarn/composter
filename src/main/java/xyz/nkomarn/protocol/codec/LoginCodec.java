package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketLogin;
import xyz.nkomarn.util.ByteBufUtil;

import java.io.IOException;

public class LoginCodec extends Codec<PacketLogin> {
    public LoginCodec() {
        super(PacketLogin.class, 0x01);
    }

    @Override
    public ByteBuf encode(PacketLogin packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(packet.getId());
        ByteBufUtil.writeString(buffer, "");
        buffer.writeLong(packet.getSeed());
        buffer.writeByte(packet.getDimension());
        return buffer;
    }

    @Override
    public PacketLogin decode(ByteBuf buffer) throws IOException {
        final int id = buffer.readInt();
        final String username = ByteBufUtil.readString(buffer);
        final long seed = buffer.readLong();
        final int dimension = buffer.readByte();
        return new PacketLogin(id, username, seed, dimension);
    }
}
