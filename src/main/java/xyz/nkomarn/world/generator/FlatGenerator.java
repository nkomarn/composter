package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.world.noise.OpenSimplexNoise;

public class FlatGenerator implements WorldGenerator {
    @Override
    public Chunk generate(final int xx, final int zz) {
        Chunk chunk = new Chunk(xx, zz);
        OpenSimplexNoise noise = new OpenSimplexNoise();

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    //float f = (float)noise.eval(x /10.0f,  z/10.0f);
                    //int yeh = (int) ((f*3.0f) + y);

                    if (y < 5) chunk.setBlock(x, y, z, 2);
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
