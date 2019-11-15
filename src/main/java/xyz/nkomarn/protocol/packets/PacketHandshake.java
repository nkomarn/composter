package xyz.nkomarn.protocol.packets;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

public class PacketHandshake extends Packet {

    private String username;

    public PacketHandshake() {}

    public PacketHandshake(final String username) {
        this.username = username;
    }

    public void decode(final ByteBuf buffer) {
        this.username = BufferUtil.readUtf8String(buffer);
    }

}
