package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Block;
import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.type.Material;

public class FlatGenerator implements WorldGenerator {

    @Override
    public Chunk generate(final int x, final int z) {
        Chunk chunk = new Chunk(x, z);
        int y = 0;

        while (y < 10) {
            for (int xx = 0; xx < 16; xx++) {
                for (int zz = 0; zz < 16; zz++) {
                    if (y < 9) {
                        //chunk.setBlock(new Block(chunk, xx, y, zz, Material.DIRT)); //dirt
                    } else {
                        //chunk.setBlock(new Block(chunk, xx, y, zz, Material.DIRT)); //grass
                    }
                }
            }
            y++;
        }
        return chunk;
    }
}
