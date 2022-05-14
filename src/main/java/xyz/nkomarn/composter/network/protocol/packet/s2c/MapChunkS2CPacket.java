package xyz.nkomarn.composter.network.protocol.packet.s2c;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.network.protocol.Packet;

import java.util.zip.Deflater;

public class MapChunkS2CPacket extends Packet<MapChunkS2CPacket> {

    private int x;
    private int z;
    private short y;
    private byte[] data;

    public MapChunkS2CPacket() {
    }

    public MapChunkS2CPacket(int x, short y, int z, byte[] data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    @Override
    public int getId() {
        return 0x33;
    }

    @Override
    public @NotNull ByteBuf encode() {
        Deflater deflater = new Deflater(Deflater.BEST_SPEED); // TODO maybe make this configurable??
        deflater.setInput(data);
        deflater.finish();

        byte[] compressedData = new byte[81920];
        int compressedBytes = deflater.deflate(compressedData);

        if (compressedBytes == 0) {
            throw new IllegalStateException("Faulty chunk compression: not all chunk bytes were compressed.");
        }

        deflater.end();
        return Unpooled.buffer()
                .writeInt(x)
                .writeShort(y)
                .writeInt(z)
                .writeByte(15)
                .writeByte(127)
                .writeByte(15)
                .writeInt(compressedBytes)
                .writeBytes(compressedData, 0, compressedBytes);
    }
}
