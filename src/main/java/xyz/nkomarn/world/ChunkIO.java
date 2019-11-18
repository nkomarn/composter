package xyz.nkomarn.world;

import xyz.nkomarn.nbt.ByteArrayTag;
import xyz.nkomarn.nbt.CompoundTag;
import xyz.nkomarn.nbt.NBTInputStream;
import xyz.nkomarn.nbt.Tag;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.world.region.RegionFile;
import xyz.nkomarn.world.region.RegionFileCache;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ChunkIO {

    private File directory = new File("world");
    private RegionFileCache cache = new RegionFileCache();

    public Chunk read(final int x, final int z) throws IOException {
        RegionFile regionFile = cache.getRegionFile(directory, x, z);
        int regionX = x & 31;
        int regionZ = z & 31;

        if (!regionFile.hasChunk(regionX, regionZ)) return null;

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
        return chunk;
    }

    public void write(final int x, final int z, final Chunk chunk) throws IOException {
        // TODO
    }

}
