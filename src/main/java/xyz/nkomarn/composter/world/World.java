package xyz.nkomarn.composter.world;

import kyta.composter.Tickable;
import kyta.composter.math.Vec3d;
import kyta.composter.protocol.packet.play.ClientboundSetTimePacket;
import kyta.composter.world.BlockPos;
import kyta.composter.world.ChunkPos;
import kyta.composter.world.PosKt;
import kyta.composter.world.block.BlockState;
import kyta.composter.world.block.BlocksKt;
import kyta.composter.world.chunk.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.composter.entity.Entity;
import xyz.nkomarn.composter.entity.Pig;
import xyz.nkomarn.composter.entity.Player;
import xyz.nkomarn.composter.network.protocol.Packet;
import xyz.nkomarn.composter.network.protocol.packet.play.ClientboundUpdateBlockPacket;
import kyta.composter.server.MinecraftServer;
import xyz.nkomarn.composter.world.generator.WorldGenerator;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadLocalRandom;

public class World implements Tickable {
    private static final int SIMULATION_DISTANCE = 8;
    private static final int MAX_WORLD_HEIGHT = 127;
    private static final int MAX_CHUNK_ACTIONS_PER_TICK = 8;

    private BlockPos spawnPos = new BlockPos(0, MAX_WORLD_HEIGHT, 0);
    public final MinecraftServer server;
    private final Properties properties;
    private final Executor thread;
    private final Map<Long, Chunk> loadedChunks;
    private final HashMap<UUID, Entity> entities;
    private final long seed;
    private int chunkActionsThisTick;
    private long time = 0;
    private final Set<ChunkPos> pendingChunks;

    public World(@NotNull MinecraftServer server, @NotNull Properties properties, @NotNull Executor thread) {
        this.server = server;
        this.properties = properties;
        this.thread = thread;
        this.loadedChunks = new ConcurrentHashMap<>();
        this.entities = new HashMap<>();
        this.seed = ThreadLocalRandom.current().nextLong();
        this.pendingChunks = ConcurrentHashMap.newKeySet();

        createSpawnChunks();

        // spawn a pig
        var pig = new Pig(this);
        pig.setPos(new Vec3d(spawnPos.up(1)));
        addEntity(pig);
    }

    public UUID getUUID() {
        return properties.uuid;
    }

    public Properties getProperties() {
        return properties;
    }

    /**
     * requires the chunk to be currently loaded.
     */
    @NotNull
    public BlockState getBlock(BlockPos pos) {
        var chunkPos = new ChunkPos(pos);
        var chunk = getLoadedChunk(chunkPos);

        if (chunk == null) {
            return BlocksKt.getDefaultState(BlocksKt.getAIR());
        }

        return chunk.getBlock(pos);
    }

    public void setBlock(BlockPos pos, BlockState state) {
        var chunkPos = new ChunkPos(pos);
        var chunk = getLoadedChunk(chunkPos);

        if (chunk != null) {
            chunk.setBlock(pos, state);
            broadcast(new ClientboundUpdateBlockPacket(pos, state));
        }
    }

    @Nullable
    public Chunk getLoadedChunk(ChunkPos key) {
        return loadedChunks.getOrDefault(key.getCompact(), null);
    }

    @Nullable
    public Chunk getLoadedChunk(long key) {
        return loadedChunks.getOrDefault(key, null);
    }

    public void broadcast(Packet<?> packet) {
        for (var entity : entities.values()) {
            if (entity instanceof Player player) {
                // player.connection().sendPacket(packet); // todo - net refactor
            }
        }
    }

    @NotNull
    public CompletableFuture<Chunk> getChunk(int x, int z) {
        var chunk = loadedChunks.get(PosKt.asCompactChunkKey(x, z));

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

    public boolean isChunkLoaded(ChunkPos pos) {
        return loadedChunks.containsKey(PosKt.asCompactChunkKey(pos.getX(), pos.getZ()));
    }

    public boolean isChunkLoaded(int x, int z) {
        return loadedChunks.containsKey(PosKt.asCompactChunkKey(x, z));
    }

    public BlockPos getSpawn() {
        return spawnPos;
    }

    @Nullable
    public Entity getEntity(int id) {
        return entities.values().stream().filter(entity -> entity.getId() == id).findFirst().orElse(null);
    }

    public Collection<Entity> getEntities() {
        return entities.values();
    }

    public void addEntity(@NotNull Entity entity) {
        entities.put(entity.getUuid(), entity);
    }

    @Override
    public void tick(long currentTick) {
        time += 1; // TODO change to just ++

        entities.values().removeIf(Entity::isRemoved);
        entities.values().forEach(entity -> entity.tick(currentTick));

        if (currentTick % 20 == 0) {
            server.getPlayerList().onlinePlayers().stream()
                    .filter(player -> player.getWorld().getUUID().equals(properties.uuid))
                    .forEach(player -> player.connection.sendPacket(new ClientboundSetTimePacket(time)));
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

            var chunkPos = new ChunkPos(player.getBlockPos());

            for (var x = (chunkPos.getX() - SIMULATION_DISTANCE); x < (chunkPos.getX() + SIMULATION_DISTANCE); x++) {
                for (var z = (chunkPos.getZ() - SIMULATION_DISTANCE); z < (chunkPos.getZ() + SIMULATION_DISTANCE); z++) {
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
        for (var x = -7; x < 7; x++) {
            for (var z = -7; z < 7; z++) {
                getChunk(x, z).join();
            }
        }

        /*
         * find the lowest suitable spawn block.
         */
        var pos = new BlockPos(0, MAX_WORLD_HEIGHT, 0);
        while (pos.getY() > 0) {
            var block = getBlock(pos).getBlock();
            if (block != BlocksKt.getAIR()) {
                break;
            }

            pos = pos.down(1);
        }

        spawnPos = pos.up(3);
        server.getLogger().info("the default world spawn is now ({}, {}, {})", spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
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
