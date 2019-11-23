package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketPreChunk extends Packet {
    private final int x, z;
    private final boolean mode;

    public PacketPreChunk(final int x, final int z, final boolean mode) {
        this.x = x;
        this.z = z;
        this.mode = mode;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public boolean getMode() {
        return this.mode;
    }
}
