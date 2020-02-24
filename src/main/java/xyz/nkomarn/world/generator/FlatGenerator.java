package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.world.noise.OpenSimplexNoise;

public class FlatGenerator implements WorldGenerator {


    public static void main(String[] args){
        OpenSimplexNoise noise = new OpenSimplexNoise();
        int x = 0;
        int y = 0;
        int z = 1;

        float f = (float)noise.eval(x, y, z);
        System.out.print(f);
    }

    @Override
    public Chunk generate(final int xx, final int zz) {
        Chunk chunk = new Chunk(xx, zz);
        OpenSimplexNoise noise = new OpenSimplexNoise();

        int total = 0;
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {
                    float f = (float)noise.eval(x /10.0f,  z/10.0f);
                    int yeh = (int) ((f*3.0f) + y);

                    if (y < 5) chunk.setBlock(x, yeh, z, 2); // BEDROCK
                    // This is just a custom flat world generator, nothing fancy
                    else chunk.setBlock(x, yeh, z, 0);

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
