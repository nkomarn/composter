package xyz.nkomarn.composter.server;

import kyta.composter.Tickable;
import kyta.composter.server.MinecraftServer;
import kyta.composter.world.BlockPos;
import kyta.composter.world.World;
import kyta.composter.world.dimension.DimensionType;
import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.world.ChunkIO;
import xyz.nkomarn.composter.world.generator.FlatGenerator;

import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

public class WorldManager implements Tickable {
    private final MinecraftServer server;
    private final Path directory;
    private final HashMap<DimensionType, World> worlds;

    public WorldManager(@NotNull MinecraftServer server, @NotNull Path directory) {
        this.server = server;
        this.directory = directory;
        this.worlds = new HashMap<>();
    }

    public World primaryWorld() {
        return worlds.values().stream().findFirst().orElseThrow();
    }

    public void load() {
        directory.toFile().mkdirs();

        // TODO temporarily create just 1 world
        var world = new World(server, new World.Properties(
                DimensionType.OVERWORLD,
                ThreadLocalRandom.current().nextLong(),
                new ChunkIO(server, directory.resolve("world")),
                FlatGenerator.INSTANCE,
                new BlockPos(0, 62, 0)
        ));

        worlds.put(world.getProperties().getType(), world);
    }

    @Override
    public void tick(long currentTick) {
        worlds.values().forEach(world -> world.tick(currentTick));
    }
}
