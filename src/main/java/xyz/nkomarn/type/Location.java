package xyz.nkomarn.type;

import xyz.nkomarn.world.World;

public class Location {
    private final double x, y, z;
    private float pitch, yaw;
    private final World world;

    public Location(final World world, final double x, final double y, final double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location(final World world, final double x, final double y, final double z,
                    final float yaw, final float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
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

    public World getWorld () {
        return this.world;
    }
}
