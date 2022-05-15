package xyz.nkomarn.composter.world;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.server.MinecraftServer;
import xyz.nkomarn.composter.tick.Tickable;
import xyz.nkomarn.composter.world.chunk.ChunkMap;

public class World implements Tickable {

    private final MinecraftServer server;
    private final WorldProperties properties;
    private final ChunkMap chunkMap;

    public World(MinecraftServer server, WorldProperties properties, ChunkMap chunkMap) {
        this.server = server;
        this.properties = properties;
        this.chunkMap = chunkMap;
    }

    @NotNull
    public WorldProperties properties() {
        return properties;
    }

    @Override
    public void tick() {
        chunkMap.tick();
    }
}
