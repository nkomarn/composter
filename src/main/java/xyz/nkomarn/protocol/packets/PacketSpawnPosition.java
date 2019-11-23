package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketSpawnPosition extends Packet {
    private final int x, y,z;

    public PacketSpawnPosition(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }
}
