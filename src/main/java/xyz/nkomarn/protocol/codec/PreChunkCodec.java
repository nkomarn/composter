package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketPreChunk;

import java.io.IOException;

public class PreChunkCodec extends Codec<PacketPreChunk> {
    public PreChunkCodec() {
        super(PacketPreChunk.class, 0x32);
    }

    @Override
    public ByteBuf encode(PacketPreChunk packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(packet.getX());
        buffer.writeInt(packet.getZ());
        buffer.writeBoolean(packet.getMode());
        return buffer;
    }

    @Override
    public PacketPreChunk decode(ByteBuf buffer) throws IOException {
        final int x = buffer.readInt();
        final int z = buffer.readInt();
        final boolean mode = buffer.readBoolean();
        return new PacketPreChunk(x, z, mode);
    }
}
