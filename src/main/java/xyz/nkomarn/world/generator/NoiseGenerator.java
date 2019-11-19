package xyz.nkomarn.world.generator;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.world.noise.PerlinNoise;

public class NoiseGenerator implements WorldGenerator {
    @Override
    public Chunk generate(int xx, int zz) {
        Chunk chunk = new Chunk(xx, zz);

        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 128; y++) {
                for (int z = 0; z < 16; z++) {

                    int value = (int) Math.floor(Math.max(2, Noise.valueCoherentNoise3D(x, y, z, 84636, NoiseQuality.STANDARD) * 128));

                    //System.out.println(value);

                    chunk.setBlock(x, value, z, 2);

                    chunk.setMetaData(x, value, z, 0);
                    chunk.setSkyLight(x, value, z, 13);
                    chunk.setBlockLight(x, value, z, 13);

                    //System.out.println(String.format("Generated block %s, %s, %s", x, y, z));
                }
            }
        }
        return chunk;
    }
}
