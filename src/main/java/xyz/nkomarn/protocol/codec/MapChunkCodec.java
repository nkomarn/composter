package xyz.nkomarn.protocol.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import xyz.nkomarn.Composter;
import xyz.nkomarn.protocol.Codec;
import xyz.nkomarn.protocol.packets.PacketMapChunk;

import java.io.IOException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;

public class MapChunkCodec extends Codec<PacketMapChunk> {
    public MapChunkCodec() {
        super(PacketMapChunk.class, 0x33);
    }

    @Override
    public ByteBuf encode(PacketMapChunk packet) throws IOException {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(packet.getX());
        buffer.writeShort(packet.getY());
        buffer.writeInt(packet.getZ());
        buffer.writeByte(15);
        buffer.writeByte(127);
        buffer.writeByte(15);

        // Compress chunk data TODO move off thread
        final byte[] data = packet.getData();
        byte[] compressedData = new byte[81920];

        Deflater deflater = new Deflater(Deflater.BEST_SPEED);
        deflater.setInput(data);
        deflater.finish();

        int compressed = deflater.deflate(compressedData);
        try {
            if (compressed == 0) { // TODO throw IOException
                Composter.getLogger().error("Not all chunk bytes were compressed.");
            }
        } finally {
            deflater.end();
        }

        buffer.writeInt(compressed);
        buffer.writeBytes(compressedData, 0, compressed);
        return buffer;
    }

    @Override
    public PacketMapChunk decode(ByteBuf buffer) throws IOException {
        return null; // Server to client only
    }
}
