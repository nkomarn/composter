package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketPlayerPosition extends Packet {
    private double x, y, stance, z;
    private boolean onGround;

    public PacketPlayerPosition(final double x, final double y, final double stance, final double z,
                                final boolean onGround) {
        this.x = x;
        this.y = y;
        this.stance = stance;
        this.z = z;
        this.onGround = onGround;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getStance() {
        return this.stance;
    }

    public double getZ() {
        return this.z;
    }

    public boolean isOnGround() {
        return this.onGround;
    }
}
