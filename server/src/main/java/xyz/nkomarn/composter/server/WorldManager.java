package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.world.ChunkIO;
import xyz.nkomarn.composter.world.World;
import xyz.nkomarn.composter.world.generator.BetaGenerator;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorldManager {

    private final Composter server;
    private final Path directory;

    private final HashMap<UUID, World> worlds;
    private final ExecutorService chunkThread;

    public WorldManager(@NotNull Composter server, @NotNull Path directory) {
        this.server = server;
        this.directory = directory;
        this.worlds = new HashMap<>();
        this.chunkThread = Executors.newFixedThreadPool(3); // TODO configurable
    }

    public void load() {
        directory.toFile().mkdirs();

        // TODO temporarily create just 1 world
        worlds.put(UUID.randomUUID(), new World(server, new World.Properties(
                UUID.randomUUID(),
                new ChunkIO(server, directory.resolve("world"), chunkThread),
                new BetaGenerator(server)
                //new FlatGenerator()
        ), chunkThread));
    }

    public Optional<World> getWorld(@NotNull UUID uuid) {
        return Optional.ofNullable(worlds.get(uuid));
    }

    public Collection<World> getWorlds() {
        return worlds.values();
    }

    public void tick() {
        this.worlds.values().forEach(World::tick);
    }
}
