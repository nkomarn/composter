package xyz.nkomarn.composter.world;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.Composter;
import xyz.nkomarn.composter.type.Chunk;
import xyz.nkomarn.composter.world.region.RegionFile;
import xyz.nkomarn.composter.world.region.RegionFileCache;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class ChunkIO {

    private final Composter server;
    private final Path directory;
    private final RegionFileCache cache;
    private final ExecutorService thread;

    public ChunkIO(@NotNull Composter server, @NotNull Path directory, @NotNull ExecutorService thread) {
        this.server = server;
        this.directory = directory;
        this.cache = new RegionFileCache();
        this.thread = thread;
    }

    public CompletableFuture<Chunk> read(int x, int z) {
        CompletableFuture<Chunk> future = new CompletableFuture<>();
        thread.submit(() -> {
            /*try {
                RegionFile regionFile = cache.getRegionFile(directory.toFile(), x, z);

                int regionX = x & 31;
                int regionZ = z & 31;

                if (!regionFile.hasChunk(regionX, regionZ)) {
                    future.complete(null);
                }

                DataInputStream inputStream = regionFile.getChunkDataInputStream(regionX, regionZ);
                Chunk chunk = new Chunk(x, z);

                NBTInputStream nbtInputStream = new NBTInputStream(inputStream, false);
                CompoundTag compoundTag = (CompoundTag) nbtInputStream.readTag();
                Map<String, Tag> levelTags = ((CompoundTag) compoundTag.getValue().get("Level")).getValue();

                byte[] tileData = ((ByteArrayTag) levelTags.get("Blocks")).getValue();
                chunk.setBlocks(tileData);

                byte[] skyLightData = ((ByteArrayTag) levelTags.get("SkyLight")).getValue();
                byte[] blockLightData = ((ByteArrayTag) levelTags.get("BlockLight")).getValue();
                byte[] metaData = ((ByteArrayTag) levelTags.get("Data")).getValue();

                for (int cx = 0; cx < 16; cx++) {
                    for (int cz = 0; cz < 16; cz++) {
                        for (int cy = 0; cy < 128; cy++) {
                            final int i = (cx * 16 + cz) * 128 + cy;
                            boolean mostSignificantNibble = i % 2 == 1;
                            int offset = i / 2;

                            int skyLight, blockLight, meta;
                            if (mostSignificantNibble) {
                                skyLight = (skyLightData[offset] & 0xF0) >> 4;
                                blockLight = (blockLightData[offset] & 0xF0) >> 4;
                                meta = (metaData[offset] & 0xF0) >> 4;
                            } else {
                                skyLight = skyLightData[offset] & 0x0F;
                                blockLight = blockLightData[offset] & 0x0F;
                                meta = metaData[offset] & 0x0F;
                            }

                            chunk.setSkyLight(cx, cz, cy, skyLight);
                            chunk.setBlockLight(cx, cz, cy, blockLight);
                            chunk.setMetaData(cx, cz, cy, meta);
                        }
                    }
                }

                nbtInputStream.close();
                future.complete(chunk);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        });
        return future;
    }

    public void write(final int x, final int z, final Chunk chunk) throws IOException {
        // TODO
    }

}
