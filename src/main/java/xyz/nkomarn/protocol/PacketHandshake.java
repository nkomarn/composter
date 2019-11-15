package xyz.nkomarn.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import xyz.nkomarn.util.BufferUtil;

public abstract class PacketHandshake extends Packet {

    private final String username;

    // Decode
    public PacketHandshake(ByteBuf buffer) {
        this.username = BufferUtil.readString(buffer);
    }

    // Encode
    public PacketHandshake() {
    }

    public PacketHandshake(final String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

}
