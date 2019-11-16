package xyz.nkomarn.world;

import xyz.nkomarn.model.Chunk;

public interface WorldGenerator {
    public Chunk generate(final int x, final int z);
}
