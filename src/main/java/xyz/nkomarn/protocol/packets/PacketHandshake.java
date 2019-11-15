package xyz.nkomarn.protocol.packets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Packet;
import xyz.nkomarn.util.BufferUtil;

public class PacketHandshake extends Packet<PacketHandshake> {

    private String message;

    private PacketHandshake(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public ByteBuf encode(PacketHandshake message) {
        ByteBuf buffer = Unpooled.buffer();
        BufferUtil.writeString(buffer, message.getMessage());
        return buffer;
    }

    @Override
    public PacketHandshake decode(ByteBuf buffer) {
        String message = BufferUtil.readString(buffer);
        return new PacketHandshake(message);
    }
}
