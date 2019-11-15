package xyz.nkomarn.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

public class PacketLoginRequest extends Packet {

    private final int version;
    private final String username;

    // Decode
    public PacketLoginRequest(ByteBuf buffer) {
        this.version = buffer.readInt();
        this.username = BufferUtil.readString(buffer);
    }

    public PacketLoginRequest(final int version, final String username) {
        this.version = version;
        this.username = username;
    }

    public ByteBuf getBuffer() {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(this.version);
        BufferUtil.writeString(buffer, this.username);
        return buffer;
    }

    public int getVersion() {
        return this.version;
    }

    public String getUsername() {
        return this.username;
    }

}
