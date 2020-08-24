package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Chunk;

import java.util.List;
import java.util.Random;

public class Populator {
    Random rand = new Random();

    public void generateFlowers(List<Integer[]> item, Chunk terrain, int z, int y, int x){
        for (Integer[] blocks : item) {
            for (int j = 0; j < 8; j++) {
                for (int k = 0; k < 8; k++) {
                    int randNum = rand.nextInt(100);
                    int zz = blocks[0] + j - 1;
                    int yy = blocks[1] - 1;
                    int xx = blocks[2] + k - 1;

                    if (z == zz && y == yy && x == xx) {
                        if (terrain.getType(z, y, x) == 2 && terrain.getType(z, y + 1, x) != 39) {
                            if (randNum < 7) {
                                terrain.setBlock(z, y + 1, x, 37);
                            }
                        }
                    }
                }
            }
        }
    }

    public void generateSugarCanes(List<Integer[]> item, Chunk terrain){
        for(Integer[] block : item){
            outerLoop:
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    for (int y = 127; y >= 0; y--) {

                        for (int j = 0; j < 3; j++) {
                            for (int k = 0; k < 3; k++) {
                                int zz = block[0] + j - 1;
                                int yy = block[1] - 1;
                                int xx = block[2] + k - 1;

                                if (z == zz && y == yy && x == xx) {
                                    if (terrain.getType(z, y, x) == 8) {
                                        for (int i = 0; i < rand.nextInt(4 + 1 - 2) + 2; i++) {
                                            terrain.setBlock(block[0], block[1] + i, block[2], 83);
                                        }
                                        break outerLoop;
                                    }else{
                                        terrain.setBlock(block[0], block[1], block[2], 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void generateOakTree(List<Integer[]> item, Chunk terrain){
        for (Integer[] blocks : item) {
            for(int x = 0; x < 16; x++) {
                for(int z = 0; z < 16; z++) {
                    for (int y = 127; y >= 0; y--) {

                        for (int j = 0; j < 3; j++) {
                            for (int k = 0; k < 3; k++) {
                                int randNum = rand.nextInt(100);
                                int zz = blocks[0] + j - 1;
                                int yy = blocks[1];
                                int xx = blocks[2] + k - 1;

                                terrain.setBlock(blocks[0], blocks[1], blocks[2], 18);
                                if (z == zz && y == yy && x == xx) {
                                    if(terrain.getType(z, y, x) == 0 && terrain.getType(z, y, x) != 17){
                                        if(k == 1 || j == 1){
                                            if(j + k != 2 && randNum < 50){
                                                terrain.setBlock(z, y, x, 18);
                                            }
                                            terrain.setBlock(z, y - 1, x, 18);
                                        }
                                     }
                                }
                            }
                        }

                        for (int j = 0; j < 5; j++) {
                            for (int k = 0; k < 5; k++) {
                                int randInt = rand.nextInt(100);
                                int zz = blocks[0] + j - 2;
                                int yy = blocks[1];
                                int xx = blocks[2] + k - 2;

                                if (z == zz && y == yy && x == xx) {
                                    if(terrain.getType(z, y, x) == 0){
                                        if(j + k == 0 || j + k == 4 || j + k == 8){
                                            if(randInt < 30){
                                                terrain.setBlock(z, y - 2, x, 18);
                                            } else {
                                                terrain.setBlock(z, y - 3, x, 18);
                                            }
                                        } else {
                                            terrain.setBlock(z, y - 2, x, 18);
                                            terrain.setBlock(z, y - 3, x, 18);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
