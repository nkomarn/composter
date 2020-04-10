package xyz.nkomarn.type;

import xyz.nkomarn.world.World;

public abstract class Entity {
    //protected final World world;
    protected boolean alive = true;

    protected World world;
    protected Location location;

    public Entity(final World world) {
        this.world = world;
        this.location = new Location(world, 0, 15, 0);
        // TODO add entity to world entities
    }
}
