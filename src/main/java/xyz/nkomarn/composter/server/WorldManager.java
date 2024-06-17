package xyz.nkomarn.composter.server;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.world.ChunkIO;
import xyz.nkomarn.composter.world.World;
import xyz.nkomarn.composter.world.generator.FlatGenerator;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

public class WorldManager {

    private final Composter server;
    private final Path directory;

    private final HashMap<UUID, World> worlds;
    private final Executor chunkThread;

    public WorldManager(@NotNull Composter server, @NotNull Path directory) {
        this.server = server;
        this.directory = directory;
        this.worlds = new HashMap<>();
        this.chunkThread = Runnable::run; // Executors.newVirtualThreadPerTaskExecutor();// TODO configurable
    }

    public void load() {
        directory.toFile().mkdirs();

        // TODO temporarily create just 1 world
        var seed = ThreadLocalRandom.current().nextLong(Long.MIN_VALUE, Long.MAX_VALUE);
        var world = new World(server, new World.Properties(
                UUID.randomUUID(),
                seed,
                new ChunkIO(server, directory.resolve("world"), chunkThread),
                FlatGenerator.INSTANCE),
                //new FlatGenerator(),
                chunkThread
        );
        worlds.put(UUID.randomUUID(), world);
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
