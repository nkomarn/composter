package xyz.nkomarn.composter.world;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.world.generator.ChunkGenerator;

import java.util.UUID;

public class WorldProperties {

    private final UUID uuid;
    private final long seed;
    private final ChunkGenerator generator;

    public WorldProperties(UUID uuid, long seed, ChunkGenerator generator) {
        this.uuid = uuid;
        this.seed = seed;
        this.generator = generator;
    }

    @NotNull
    public UUID uuid() {
        return uuid;
    }

    public long seed() {
        return seed;
    }

    public ChunkGenerator generator() {
        return generator;
    }
}
