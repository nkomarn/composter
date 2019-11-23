package xyz.nkomarn.protocol.packets;

import xyz.nkomarn.protocol.Packet;

public class PacketPlayerPositionAndLook extends Packet {
    private final double x, y, stance, z;
    private final float yaw, pitch;
    private boolean onGround;

    public PacketPlayerPositionAndLook(final double x, final double y, final double stance, final double z,
                                       final float yaw, final float pitch, final boolean onGround) {
        this.x = x;
        this.y = y;
        this.stance = stance;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
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

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public boolean isOnGround() {
        return this.onGround;
    }
}
