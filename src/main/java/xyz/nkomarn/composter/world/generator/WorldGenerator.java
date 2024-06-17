package xyz.nkomarn.composter.world.generator;

import kyta.composter.world.chunk.Chunk;

public interface WorldGenerator {
    Chunk generate(final int x, final int z);
}
