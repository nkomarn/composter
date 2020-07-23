package xyz.nkomarn.world;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.world.generator.WorldGenerator;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class World {
    public final Location spawn = new Location(this, 0, 15, 0); // TODO implement for player spawning at some point
    private final HashMap<Chunk.Key, Chunk> loadedChunks = new HashMap<>();
    // TODO entities list

    private final Properties properties;
    private final ExecutorService thread;

    private long time = 0;

    public World(@NotNull Properties properties, @NotNull ExecutorService thread) {
        this.properties = properties;
        this.thread = thread;
    }

    public CompletableFuture<Chunk> getChunk(int x, int z) {
        CompletableFuture<Chunk> future = new CompletableFuture<>();
        thread.submit(() -> {
            Chunk.Key key = new Chunk.Key(x, z);
            Chunk chunk = loadedChunks.get(key);

            if (chunk == null) {
                properties.getIO().read(x, z).thenAccept(futureChunk -> {
                    if (futureChunk != null) {
                        future.complete(futureChunk);
                        return;
                    }

                    Chunk newChunk = properties.getGenerator().generate(x, z);
                    loadedChunks.put(key, newChunk);
                    future.complete(newChunk);
                });
            } else {
                future.complete(chunk);
            }
        });
        return future;
    }

    // TODO save chunks, etc

    public static class Properties {

        private final UUID uuid;
        private final ChunkIO io;
        private final WorldGenerator generator;

        public Properties(@NotNull UUID uuid, @NotNull ChunkIO io, @NotNull WorldGenerator generator) {
            this.uuid = uuid;
            this.io = io;
            this.generator = generator;
        }

        public @NotNull UUID getUUID() {
            return uuid;
        }

        public @NotNull ChunkIO getIO() {
            return io;
        }

        public @NotNull WorldGenerator getGenerator() {
            return generator;
        }
    }
}
