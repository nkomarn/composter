package xyz.nkomarn.world.generator;

import xyz.nkomarn.object.Chunk;

public class FlatGenerator implements WorldGenerator {
    
    @Override
    public Chunk generate(final int x, final int z) {
        Chunk chunk = new Chunk(x, z);
        int y = 0;

        while (y < 10) {
            for (int xx = 0; xx < 16; xx++) {
                for (int zz = 0; zz < 16; zz++) {
                    if (y < 9) {
                        chunk.setBlock(xx, y, zz, 3); //dirt
                    } else {
                        chunk.setBlock(xx, y, zz, 2); //grass
                    }
                }
            }
            y++;
        }
        return chunk;
    }
}
