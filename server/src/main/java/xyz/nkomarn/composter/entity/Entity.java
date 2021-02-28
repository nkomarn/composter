package xyz.nkomarn.composter.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.type.Chunk;
import xyz.nkomarn.composter.type.Location;
import xyz.nkomarn.composter.world.World;

import java.util.UUID;

public abstract class Entity {

    protected boolean alive = true;

    protected World world;
    protected Location location;
    protected UUID uuid;

    public Entity(@NotNull World world) {
        this.world = world;
        this.location = new Location(world, 0, 15, 0);
        this.uuid = UUID.randomUUID();
        // TODO add entity to world entities
    }

    public boolean isTouchingGround() {
        Chunk.Key chunk = location.getChunk();
        if (world.isChunkLoaded(chunk)) {
            return false;
        }

        return world.getChunkImmediately(chunk.getX(), chunk.getZ()).getType(
                location.getBlockX(),
                location.getBlockY() - 1,
                location.getBlockZ()
        ) != 0;
    }

    public int getId() {
        return Math.abs(uuid.hashCode()); // TODO temporary
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void tick() {
    }
}
