package xyz.nkomarn.composter.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.type.Location;
import xyz.nkomarn.composter.world.World;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Entity {


    private static final AtomicInteger ENTITY_COUNTER = new AtomicInteger();

    private final int id;
    protected World world;
    protected Location location;
    protected UUID uuid;
    private boolean removed;

    public Entity(@NotNull World world) {
        this.id = ENTITY_COUNTER.getAndIncrement();
        this.world = world;
        this.location = new Location(world, 0, 15, 0);
        this.uuid = UUID.randomUUID();
        // TODO add entity to world entities
    }

    public int getId() {
        return id;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void markRemoved() {
        this.removed = true;
    }

    public void tick() {
    }
}
