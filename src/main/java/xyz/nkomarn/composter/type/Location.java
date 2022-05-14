package xyz.nkomarn.composter.type;

import xyz.nkomarn.composter.world.World;

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

    public int getBlockX() {
        return (int) Math.floor(this.x);
    }

    public int getBlockY() {
        return (int) Math.floor(this.y);
    }

    public int getBlockZ() {
        return (int)  Math.floor(this.z);
    }

    public World getWorld () {
        return this.world;
    }

    public Chunk.Key getChunk() {
       return new Chunk.Key((int) Math.floor(x / 16), (int) Math.floor(z / 16));
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
