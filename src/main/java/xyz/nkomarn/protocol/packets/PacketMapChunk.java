package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketMapChunk extends Packet {
    private final int x, z;
    private final short y;
    private final byte[] data;

    public PacketMapChunk(final int x, final short y, final int z, final byte[] data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    public int getX() {
        return this.x;
    }

    public short getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public byte[] getData() {
        return this.data;
    }
}
