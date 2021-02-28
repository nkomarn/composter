package xyz.nkomarn.composter.world.generator;

import xyz.nkomarn.composter.type.Chunk;

public interface WorldGenerator {
    public Chunk generate(final int x, final int z);
}
