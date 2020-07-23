package xyz.nkomarn.type;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.world.World;

public abstract class Entity {
    protected boolean alive = true;

    protected World world;
    protected Location location;

    public Entity(@NotNull World world) {
        this.world = world;
        this.location = new Location(world, 0, 15, 0);
        // TODO add entity to world entities
    }
}
