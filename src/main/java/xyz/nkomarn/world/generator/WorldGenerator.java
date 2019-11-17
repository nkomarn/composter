package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Chunk;

public interface WorldGenerator {
    public Chunk generate(final int x, final int z);
}
