package xyz.nkomarn.type;

import xyz.nkomarn.world.World;

public abstract class Entity {
    //protected final World world;
    protected boolean alive = true;

    protected World world;
    protected Location location = new Location(0, 15, 0);

    public Entity(final World world) {
        this.world = world;
        // TODO add entity to world entities
    }
}
