package xyz.nkomarn.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.world.World;

import java.util.concurrent.ExecutionException;

public abstract class Entity {

    protected boolean alive = true;

    protected World world;
    protected Location location;

    public Entity(@NotNull World world) {
        this.world = world;
        this.location = new Location(world, 0, 15, 0);
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

    public void tick() {
    }
}
