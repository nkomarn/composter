package xyz.nkomarn.composter.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import kyta.composter.world.ChunkPos;
import kyta.composter.world.ChunkPosKt;
import kyta.composter.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundSetTimePacket;
import xyz.nkomarn.composter.type.Location;
import xyz.nkomarn.composter.world.generator.WorldGenerator;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

public class World {
    private static final int SIMULATION_DISTANCE = 8;
    private static final int MAX_CHUNK_ACTIONS_PER_TICK = 8;
    private final Location spawn = new Location(this, 0, 128, 0); // TODO implement for player spawning at some point
    // TODO entities list

    private final Composter server;
    private final Properties properties;
    private final Executor thread;
    private final Long2ObjectMap<Chunk> loadedChunks;
    private final HashMap<UUID, Entity> entities;
    private final long seed;
    private int chunkActionsThisTick;
    private long time = 0;
    private final Set<ChunkPos> pendingChunks;

    public World(@NotNull Composter server, @NotNull Properties properties, @NotNull Executor thread) {
        this.server = server;
        this.properties = properties;
        this.thread = thread;
        this.loadedChunks = new Long2ObjectOpenHashMap<>();
        this.entities = new HashMap<>();
        this.seed = ThreadLocalRandom.current().nextLong();
        this.pendingChunks = ConcurrentHashMap.newKeySet();

        createSpawnChunks();
    }

    public UUID getUUID() {
        return properties.uuid;
    }

    public Properties getProperties() {
        return properties;
    }

    @Nullable
    public Chunk getLoadedChunk(ChunkPos key) {
        return loadedChunks.get(key.getCompact());
    }

    @Nullable
    public Chunk getLoadedChunk(long key) {
        return loadedChunks.get(key);
    }

    @NotNull
    public CompletableFuture<Chunk> getChunk(int x, int z) {
        var pos = new ChunkPos(x, z);
        var chunk = loadedChunks.get(ChunkPosKt.asCompactChunkKey(x, z));

        if (chunk != null) {
            return CompletableFuture.completedFuture(chunk);
        }

        /*
         * todo; attempt to load the chunk from disk first.
         */


        /*
         * generate the chunk asynchronously.
         */
        return CompletableFuture.supplyAsync(() -> {
            var newChunk = properties.getGenerator().generate(x, z);

            try {
                properties.getIO().write(x, z, newChunk);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            loadedChunks.put(newChunk.getPos().getCompact(), newChunk);
            return newChunk;
        }, properties.getIO().getExecutor());
    }

    public boolean isChunkLoaded(int x, int z) {
        return loadedChunks.containsKey(ChunkPosKt.asCompactChunkKey(x, z));
    }

    public Location getSpawn() {
        return spawn;
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public void addEntity(@NotNull Entity entity) {
        entities.put(entity.getUUID(), entity);
    }

    public void tick() {
        time += 1; // TODO change to just ++

        entities.values().removeIf(Entity::isRemoved);

        if (server.currentTick() % 20 == 0) {
            server.getPlayerManager().getPlayers().stream()
                    .filter(player -> player.getWorld().getUUID().equals(properties.uuid))
                    .forEach(player -> player.connection().sendPacket(new ClientboundSetTimePacket(time)));
        }

        tickChunks();
    }

    /**
     * handles chunk loading and unloading for entities.
     */
    private void tickChunks() {
        chunkActionsThisTick = 0;

        for (var entity : entities.values()) {
            if (!(entity instanceof Player player)) {
                continue;
            }

            var chunkX = player.getLocation().getBlockX() >> 4;
            var chunkZ = player.getLocation().getBlockZ() >> 4;

            for (var x = (chunkX - SIMULATION_DISTANCE); x < (chunkX + SIMULATION_DISTANCE); x++) {
                for (var z = (chunkZ - SIMULATION_DISTANCE); z < (chunkZ + SIMULATION_DISTANCE); z++) {
                    if (false && chunkActionsThisTick >= MAX_CHUNK_ACTIONS_PER_TICK) {
                        break;
                    }

                    var pos = new ChunkPos(x, z);
                    if (!pendingChunks.contains(pos) && !isChunkLoaded(x, z)) {
                        pendingChunks.add(pos);
                        getChunk(x, z).thenRun(() -> pendingChunks.remove(pos)); // schedule chunk load
                        chunkActionsThisTick++;
                    }
                }
            }
        }
    }

    private void createSpawnChunks() {
        for (var x = -1; x < 1; x++) {
            for (var z = -1; z < 1; z++) {
                getChunk(x, z).join();
            }
        }
    }

    // TODO save chunks, etc

    public static class Properties {

        private final UUID uuid;
        private final long seed;
        private final ChunkIO io;
        private final WorldGenerator generator;

        public Properties(@NotNull UUID uuid, long seed, @NotNull ChunkIO io, @NotNull WorldGenerator generator) {
            this.uuid = uuid;
            this.seed = seed;
            this.io = io;
            this.generator = generator;
        }

        public @NotNull UUID getUUID() {
            return uuid;
        }

        public long getSeed() {
            return seed;
        }

        public @NotNull ChunkIO getIO() {
            return io;
        }

        public @NotNull WorldGenerator getGenerator() {
            return generator;
        }
    }
}
