package xyz.nkomarn.protocol.packets;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.protocol.Packet;

import java.io.IOException;

public class PacketKeepAlive extends Packet {
    public PacketKeepAlive() { }

    @Override
    public ByteBuf encode(Object message) {
        return null;
    }

    @Override
    public Object decode(ByteBuf buffer) {
        return null;
    }
}
