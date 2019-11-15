package xyz.nkomarn.protocol.scaffold;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.protocol.PacketLogin;
import xyz.nkomarn.util.BufferUtil;

import java.io.IOException;

public class PacketLoginScaffold extends PacketScaffold<PacketLogin> {

    public PacketLoginScaffold() {
        super(PacketLogin.class, 0x01);
    }

    @Override
    public ByteBuf encode(PacketLogin message) throws IOException {
        return null;
    }

    @Override
    public PacketLogin decode(ByteBuf buffer) throws IOException {
        final int version = buffer.readInt();
        final String username = BufferUtil.readString(buffer);
        return new PacketLogin(version, username);
    }

}
