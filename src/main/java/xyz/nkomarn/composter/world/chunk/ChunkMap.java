package xyz.nkomarn.composter.world.chunk;

import xyz.nkomarn.composter.tick.Tickable;
import xyz.nkomarn.composter.world.World;

public class ChunkMap implements Tickable {

    private final World world;

    public ChunkMap(World world) {
        this.world = world;
    }

    @Override
    public void tick() {

    }
}
