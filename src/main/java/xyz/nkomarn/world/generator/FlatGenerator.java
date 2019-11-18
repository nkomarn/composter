package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Chunk;

public class FlatGenerator implements WorldGenerator {

    @Override
    public Chunk generate(final int xx, final int zz) {
        Chunk chunk = new Chunk(xx, zz);

        int total = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {

                    if (y < 5) chunk.setBlock(x, y, z, 2); // BEDROCK
                    // This is just a custom flat world generator, nothing fancy
                    else chunk.setBlock(x, y, z, 0);

                    chunk.setMetaData(x, y, z, 0);
                    chunk.setSkyLight(x, y, z, 13);
                    chunk.setBlockLight(x, y, z, 13);

                    //System.out.println(String.format("Generated block %s, %s, %s", x, y, z));
                }
            }
        }
        return chunk;
    }
}
