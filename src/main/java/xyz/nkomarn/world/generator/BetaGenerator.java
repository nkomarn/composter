package xyz.nkomarn.world.generator;

import xyz.nkomarn.type.Chunk;
import xyz.nkomarn.world.noise.NoiseGeneratorOctaves3D;

import java.util.Random;

public class BetaGenerator implements WorldGenerator {

    private long worldSeed = 22252l;
    private Random rand;
    private NoiseGeneratorOctaves3D noiseGen1;
    private NoiseGeneratorOctaves3D noiseGen2;
    private NoiseGeneratorOctaves3D noiseGen3;
    private NoiseGeneratorOctaves3D noiseGen4;
    private NoiseGeneratorOctaves3D noiseGen5;
    private NoiseGeneratorOctaves3D noiseGen6;
    private NoiseGeneratorOctaves3D noiseGen7;
    private NoiseGeneratorOctaves3D treeNoise;

    private double noise[];
    private double sandNoise[] = new double[256];
    private double gravelNoise[] = new double[256];
    private double stoneNoise[] = new double[256];
    private double noise3[];
    private double noise1[];
    private double noise2[];
    private double noise6[];
    private double noise7[];

    public BetaGenerator() {
        rand = new Random(worldSeed);
        noiseGen1 = new NoiseGeneratorOctaves3D(rand, 16, false);
        noiseGen2 = new NoiseGeneratorOctaves3D(rand, 16, false);
        noiseGen3 = new NoiseGeneratorOctaves3D(rand, 8,  false);
        noiseGen4 = new NoiseGeneratorOctaves3D(rand, 4, false);
        noiseGen5 = new NoiseGeneratorOctaves3D(rand, 4, false);
        noiseGen6 = new NoiseGeneratorOctaves3D(rand, 10, false);
        noiseGen7 = new NoiseGeneratorOctaves3D(rand, 16, false);
        treeNoise = new NoiseGeneratorOctaves3D(rand, 8, false);
    }

    @Override
    public Chunk generate(int x, int z) {
        Chunk chunk = new Chunk(x, z);

        generateTerrain(x, z, chunk);
        replaceBlocksForBiome(x, z, chunk);
        genC(x, z, chunk);

        return chunk;
    }

    public void generateTerrain(int x, int z, Chunk chunk) {
        //double temperatures[] = this.wcm.temperatures;
        byte byte0 = 4;
        byte oceanHeight = 64;
        int k = byte0 + 1;
        byte b2 = 17;
        int l = byte0 + 1;
        noise = initNoiseField(noise, x * byte0, 0, z * byte0, k, b2, l);
        for(int xPiece = 0; xPiece < byte0; xPiece++) {
            for(int zPiece = 0; zPiece < byte0; zPiece++) {
                for(int yPiece = 0; yPiece < 16; yPiece++) {
                    double d = 0.125D;
                    double d1 = noise[((xPiece + 0) * l + (zPiece + 0)) * b2 + (yPiece + 0)];
                    double d2 = noise[((xPiece + 0) * l + (zPiece + 1)) * b2 + (yPiece + 0)];
                    double d3 = noise[((xPiece + 1) * l + (zPiece + 0)) * b2 + (yPiece + 0)];
                    double d4 = noise[((xPiece + 1) * l + (zPiece + 1)) * b2 + (yPiece + 0)];
                    double d5 = (noise[((xPiece + 0) * l + (zPiece + 0)) * b2 + (yPiece + 1)] - d1) * d;
                    double d6 = (noise[((xPiece + 0) * l + (zPiece + 1)) * b2 + (yPiece + 1)] - d2) * d;
                    double d7 = (noise[((xPiece + 1) * l + (zPiece + 0)) * b2 + (yPiece + 1)] - d3) * d;
                    double d8 = (noise[((xPiece + 1) * l + (zPiece + 1)) * b2 + (yPiece + 1)] - d4) * d;
                    for(int l1 = 0; l1 < 8; l1++) {
                        double d9 = 0.25D;
                        double d10 = d1;
                        double d11 = d2;
                        double d12 = (d3 - d1) * d9;
                        double d13 = (d4 - d2) * d9;
                        for(int i2 = 0; i2 < 4; i2++) {
                            int xLoc = i2 + xPiece * 4;
                            int yLoc = yPiece * 8 + l1;
                            int zLoc = 0 + zPiece * 4;
                            double d14 = 0.25D;
                            double d15 = d10;
                            double d16 = (d11 - d10) * d14;
                            for(int k2 = 0; k2 < 4; k2++) {
                                double d17 = 0.6d; //temperatures[(xPiece * 4 + i2) * 16 + (zPiece * 4 + k2)];
                                int type = 0; // air
                                if(yPiece * 8 + l1 < oceanHeight) {
                                    if(d17 < 0.5D && yPiece * 8 + l1 >= oceanHeight - 1) {
                                        type = 79;
                                    } else {
                                        type = 8;
                                    }
                                }
                                if(d15 > 0.0D) {
                                    type = 1;
                                }
                                chunk.setBlock(xLoc, yLoc, zLoc, type);
                                zLoc++;
                                d15 += d16;
                            }

                            d10 += d12;
                            d11 += d13;
                        }

                        d1 += d5;
                        d2 += d6;
                        d3 += d7;
                        d4 += d8;
                    }

                }

            }

        }
    }

    public void replaceBlocksForBiome(int xPos, int zPos, Chunk terrain) {
        //NOTE: in get/set block X and Z coordinates are swapped, THIS IS CORRECT
        byte oceanHeight = 64;
        double d = 0.03125D;
        sandNoise = noiseGen4.generateNoiseArray(sandNoise, xPos * 16, zPos * 16, 0.0D, 16, 16, 1, d, d, 1.0D);
        gravelNoise = noiseGen4.generateNoiseArray(gravelNoise, xPos * 16, 109.0134D, zPos * 16, 16, 1, 16, d, 1.0D, d);
        stoneNoise = noiseGen5.generateNoiseArray(stoneNoise, xPos * 16, zPos * 16, 0.0D, 16, 16, 1, d * 2D, d * 2D, d * 2D);
        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                // BetaBiome biome = biomes[x + z * 16];
                boolean sand = sandNoise[x + z * 16] + rand.nextDouble() * 0.2D > 0.0D;
                boolean gravel = gravelNoise[x + z * 16] + rand.nextDouble() * 0.2D > 3D;
                int depth = (int)(stoneNoise[x + z * 16] / 3D + 3D + rand.nextDouble() * 0.25D);
                int prevDepth = -1;
//                            topBlock = BiomeOld.top(biome);
//                            fillerBlock = BiomeOld.filler(biome);
                int topBlock = 2;
                int fillerBlock = 3;
                for(int y = 127; y >= 0; y--) {
                    if(y <= 0 + rand.nextInt(5)) {
                        terrain.setBlock(z, y, x, 7);
                        continue;
                    }
                    int block = terrain.getType(z, y, x);
                    if(block == 0) {
                        prevDepth = -1;
                        continue;
                    }
                    if(block != 1) {
                        continue;
                    }
                    if(prevDepth == -1) {
                        if(depth <= 0) {
                            topBlock = 0;
                            fillerBlock = 1;
                        } else if(y >= oceanHeight - 4 && y <= oceanHeight + 1) {
//                            topBlock = BiomeOld.top(biome);
//                            fillerBlock = BiomeOld.filler(biome);
                            topBlock = 12;
                            fillerBlock = 13;
                            if(gravel) {
                                topBlock = 0;
                                fillerBlock = 13;
                            }
                            if(sand) {
                                topBlock = 12;
                                fillerBlock = 12;
                            }
                        }
                        if(y < oceanHeight && topBlock == 0) {
                            topBlock = 8;
                        }
                        prevDepth = depth;
                        if(y >= oceanHeight - 1) {
                            terrain.setBlock(z, y, x, topBlock);
                        } else {
                            terrain.setBlock(z, y, x, fillerBlock);
                        }
                        continue;
                    }
                    if(prevDepth <= 0) {
                        continue;
                    }
                    prevDepth--;
                    terrain.setBlock(z, y, x, fillerBlock);
                    if(prevDepth == 0 && fillerBlock == 12) {
                        prevDepth = rand.nextInt(4);
                        fillerBlock = 24;
                    }
                }

            }

        }
    }

    private double[] initNoiseField(double array[], int xPos, int yPos, int zPos, int xSize, int ySize, int zSize) {
        if(array == null) {
            array = new double[xSize * ySize * zSize];
        }
        double d0 = 684.412D;
        double d1 = 684.412D;
//        double temp[] = this.wcm.temperatures;
//        double rain[] = this.wcm.rain;
        noise6 = noiseGen6.generateNoiseArray(noise6, xPos, zPos, xSize, zSize, 1.121D, 1.121D, 0.5D);
        noise7 = noiseGen7.generateNoiseArray(noise7, xPos, zPos, xSize, zSize, 200D, 200D, 0.5D);
        noise3 = noiseGen3.generateNoiseArray(noise3, xPos, yPos, zPos, xSize, ySize, zSize,
                d0 / 80D, d1 / 160D, d0 / 80D);
        noise1 = noiseGen1.generateNoiseArray(noise1, xPos, yPos, zPos, xSize, ySize, zSize,
                d0, d1, d0);
        noise2 = noiseGen2.generateNoiseArray(noise2, xPos, yPos, zPos, xSize, ySize, zSize,
                d0, d1, d0);
        int k1 = 0;
        int l1 = 0;
        int i2 = 16 / xSize;
        for(int x = 0; x < xSize; x++) {
            int k2 = x * i2 + i2 / 2;
            for(int z = 0; z < zSize; z++) {
                int i3 = z * i2 + i2 / 2;
//                double d2 = temp[k2 * 16 + i3];
//                double d3 = rain[k2 * 16 + i3] * d2;
                double d4 = 1.0D;// - d3;
                d4 *= d4;
                d4 *= d4;
                d4 = 1.0D - d4;
                double d5 = (noise6[l1] + 256D) / 512D;
                d5 *= d4;
                if(d5 > 1.0D) {
                    d5 = 1.0D;
                }
                double d6 = noise7[l1] / 8000D;
                if(d6 < 0.0D) {
                    d6 = -d6 * 0.3D;
                }
                d6 = d6 * 3D - 2D;
                if(d6 < 0.0D) {
                    d6 /= 2D;
                    if(d6 < -1D) {
                        d6 = -1D;
                    }
                    d6 /= 1.4D;
                    d6 /= 2D;
                    d5 = 0.0D;
                } else {
                    if(d6 > 1.0D) {
                        d6 = 1.0D;
                    }
                    d6 /= 8D;
                }
                if(d5 < 0.0D) {
                    d5 = 0.0D;
                }
                d5 += 0.5D;
                d6 = (d6 * (double)ySize) / 16D;
                double d7 = (double)ySize / 2D + d6 * 4D;
                l1++;
                for(int y = 0; y < ySize; y++) {
                    double d8 = 0.0D;
                    double d9 = (((double)y - d7) * 12D)
                            / d5;
                    if(d9 < 0.0D) {
                        d9 *= 4D;
                    }
                    double d10 = noise1[k1] / 512D;
                    double d11 = noise2[k1] / 512D;
                    double d12 = (this.noise3[k1] / 10D + 1.0D) / 2D;
                    if(d12 < 0.0D) {
                        d8 = d10;
                    } else if(d12 > 1.0D) {
                        d8 = d11;
                    } else {
                        d8 = d10 + (d11 - d10) * d12;
                    }
                    d8 -= d9;
                    if(y > ySize - 4) {
                        double d13 = (double)((float)(y - (ySize - 4)) / 3F);
                        d8 = d8 * (1.0D - d13) + -10D * d13;
                    }
                    array[k1] = d8;
                    k1++;
                }
            }
        }
        return array;
    }

    // Caves
    private int maxGenerationRadius = 8; // proper 8
    private Random rand2 = new Random();

    private void genC(int generatedChunkX, int generatedChunkZ, Chunk data) {
        int radius = maxGenerationRadius;
        rand2.setSeed(worldSeed);
        long l = (rand2.nextLong() / 2L) * 2L + 1L;
        long l1 = (rand2.nextLong() / 2L) * 2L + 1L;
        for(int structureOriginXhunkX = generatedChunkX - radius; structureOriginXhunkX <= generatedChunkX + radius; structureOriginXhunkX++) {
            for(int structureOriginChunkZ = generatedChunkZ - radius; structureOriginChunkZ <= generatedChunkZ + radius; structureOriginChunkZ++) {
                rand2.setSeed((long)structureOriginXhunkX * l + (long)structureOriginChunkZ * l1 ^ worldSeed);
                generateCaves(structureOriginXhunkX, structureOriginChunkZ, generatedChunkX, generatedChunkZ, data);
            }
        }
    }

    private void generateCaves(int structureOriginChunkX, int structureOriginChunkZ, int generatedChunkX, int generatedChunkZ, Chunk data) {
        int attempts = rand2.nextInt(rand2.nextInt(rand2.nextInt(40) + 1) + 1);
        if (rand2.nextInt(15) != 0) {
            attempts = 0;
        }
        for (int i = 0; i < attempts; i++) {
            double structureOriginBlockX = structureOriginChunkX * 16 + rand2.nextInt(16);
            double structureOriginBlockY = rand2.nextInt(rand2.nextInt(120) + 8);
            double structureOriginBlockZ = structureOriginChunkZ * 16 + rand2.nextInt(16);
            int branches = 1;
            if (rand2.nextInt(4) == 0) {
                generateDefaultBranch(generatedChunkX, generatedChunkZ, data, structureOriginBlockX, structureOriginBlockY, structureOriginBlockZ);
                branches += rand2.nextInt(4);
            }
            for (int branch = 0; branch < branches; branch++) {
                float directionAndgeHorizontal = rand2.nextFloat() * 3.141593F * 2.0F;
                float directionAngleVertical = ((rand2.nextFloat() - 0.5F) * 2.0F) / 8F;
                float maxHorizontalSize = rand2.nextFloat() * 2.0F + rand2.nextFloat();
                generateBranch(generatedChunkX, generatedChunkZ, data, structureOriginBlockX, structureOriginBlockY, structureOriginBlockZ, maxHorizontalSize, directionAndgeHorizontal, directionAngleVertical, 0, 0, 1.0D);
            }
        }
    }

    public static final double MIN_HORIZONTAL_SIZE = 1.5D;

    protected void generateDefaultBranch(int generatedChunkX, int generatedChunkZ, Chunk data, double structureOriginBlockX, double structureOriginBlockY, double structureOriginBlockZ) {
        generateBranch(
                generatedChunkX, generatedChunkZ,
                data,
                structureOriginBlockX, structureOriginBlockY, structureOriginBlockZ,
                1.0F + rand2.nextFloat() * 6F, 0.0F, 0.0F,
                -1, -1, 0.5D);
    }

    protected void generateBranch(int generatedChunkX, int generatedChunkZ,
                                  Chunk data,
                                  double currentBlockX, double currentBlockY, double currentBlockZ,
                                  float maxHorizontalSize, float directionAngleHorizontal, float directionAngleVertical,
                                  int currentCaveSystemRadius, int maxCaveSystemRadius, double verticalCaveSizeMultiplier) {

        double generatedChunkCenterX = generatedChunkX * 16 + 8;
        double generatedChunkCenterZ = generatedChunkZ * 16 + 8;
        float directionHorizontalChange = 0.0F;
        float directionVerticalChange = 0.0F;
        Random rand2om = new Random(rand2.nextLong());
        //negative means not generated yet
        if (maxCaveSystemRadius <= 0) {
            int maxBlockRadius = maxGenerationRadius * 16 - 16;
            maxCaveSystemRadius = maxBlockRadius - rand2om.nextInt(maxBlockRadius / 4);
        }
        boolean noSplitBranch = false;
        if (currentCaveSystemRadius == -1) {
            currentCaveSystemRadius = maxCaveSystemRadius / 2;
            noSplitBranch = true;
        }
        int splitDistance = rand2om.nextInt(maxCaveSystemRadius / 2) + maxCaveSystemRadius / 4;
        boolean allowSteepCave = rand2om.nextInt(6) == 0;
        for (; currentCaveSystemRadius < maxCaveSystemRadius; currentCaveSystemRadius++) {

            //caveRadius grows as we go out of the center
            double caveRadiusHorizontal = MIN_HORIZONTAL_SIZE + (double) (MathHelper.sin((float) currentCaveSystemRadius * 3.141593F / (float) maxCaveSystemRadius) * maxHorizontalSize * 1.0f);
            double caveRadiusVertical = caveRadiusHorizontal * verticalCaveSizeMultiplier;

            //from sin(alpha)=y/r and cos(alpha)=x/r ==> x = r*cos(alpha) and y = r*sin(alpha)
            //always moves by one block in some direction
            //x is horizontal radius, y is vertical
            float horizontalDirectionSize = MathHelper.cos(directionAngleVertical);
            float directionY = MathHelper.sin(directionAngleVertical);
            //y is directionZ and is is directionX
            currentBlockX += MathHelper.cos(directionAngleHorizontal) * horizontalDirectionSize;
            currentBlockY += directionY;
            currentBlockZ += MathHelper.sin(directionAngleHorizontal) * horizontalDirectionSize;
            if (allowSteepCave) {
                directionAngleVertical *= 0.92F;
            } else {
                directionAngleVertical *= 0.7F;
            }
            directionAngleVertical += directionVerticalChange * 0.1F;
            directionAngleHorizontal += directionHorizontalChange * 0.1F;

            directionVerticalChange *= 0.9F;
            directionHorizontalChange *= 0.75F;
            directionVerticalChange += (rand2om.nextFloat() - rand2om.nextFloat()) * rand2om.nextFloat() * 2.0F;
            directionHorizontalChange += (rand2om.nextFloat() - rand2om.nextFloat()) * rand2om.nextFloat() * 4F;

            if (!noSplitBranch && currentCaveSystemRadius == splitDistance && maxHorizontalSize > 1.0F) {
                generateBranch(generatedChunkX, generatedChunkZ, data, currentBlockX, currentBlockY, currentBlockZ,
                        rand2om.nextFloat() * 0.5F + 0.5F, directionAngleHorizontal - 1.570796F,
                        directionAngleVertical / 3F, currentCaveSystemRadius, maxCaveSystemRadius, 1.0D);
                generateBranch(generatedChunkX, generatedChunkZ, data, currentBlockX, currentBlockY, currentBlockZ,
                        rand2om.nextFloat() * 0.5F + 0.5F, directionAngleHorizontal + 1.570796F,
                        directionAngleVertical / 3F, currentCaveSystemRadius, maxCaveSystemRadius, 1.0D);
                return;
            }
            if (!noSplitBranch && rand2om.nextInt(4) == 0) {
                continue;
            }
            double chunkCenterToCurrentX = currentBlockX - generatedChunkCenterX;
            double chunkCenterToCurrentZ = currentBlockZ - generatedChunkCenterZ;

            if (isCurrentChunkUnreachable(chunkCenterToCurrentX, chunkCenterToCurrentZ, maxCaveSystemRadius, currentCaveSystemRadius, maxHorizontalSize)) {
                return;
            }
            //is cave out of bounds of current chunk?
            if (currentBlockX < generatedChunkCenterX - 16D - caveRadiusHorizontal * 2D ||
                    currentBlockZ < generatedChunkCenterZ - 16D - caveRadiusHorizontal * 2D ||
                    currentBlockX > generatedChunkCenterX + 16D + caveRadiusHorizontal * 2D ||
                    currentBlockZ > generatedChunkCenterZ + 16D + caveRadiusHorizontal * 2D) {
                continue;
            }
            int startX = MathHelper.floor(currentBlockX - caveRadiusHorizontal) - generatedChunkX * 16 - 1;
            int endX = (MathHelper.floor(currentBlockX + caveRadiusHorizontal) - generatedChunkX * 16) + 1;
            int startY = MathHelper.floor(currentBlockY - caveRadiusVertical) - 1;
            int endY = MathHelper.floor(currentBlockY + caveRadiusVertical) + 1;
            int startZ = MathHelper.floor(currentBlockZ - caveRadiusHorizontal) - generatedChunkZ * 16 - 1;
            int endZ = (MathHelper.floor(currentBlockZ + caveRadiusHorizontal) - generatedChunkZ * 16) + 1;
            if (startX < 0) {
                startX = 0;
            }
            if (endX > 16) {
                endX = 16;
            }
            if (startY < 1) {
                startY = 1;
            }
            if (endY > 120) {
                endY = 120;
            }
            if (startZ < 0) {
                startZ = 0;
            }
            if (endZ > 16) {
                endZ = 16;
            }

            if (findWater(data, startX, endX, startY, endY, startZ, endZ)) {
                continue;
            }
            for (int localX = startX; localX < endX; localX++) {
                double xDistanceScaled = ((double)(localX + generatedChunkX * 16) + 0.5D - currentBlockX) / caveRadiusHorizontal;
                for (int localZ = startZ; localZ < endZ; localZ++) {
                    double zDistanceScaled = ((double)(localZ + generatedChunkZ * 16) + 0.5D - currentBlockZ) / caveRadiusHorizontal;
                    boolean hitGrassSurface = false;
                    if (xDistanceScaled * xDistanceScaled + zDistanceScaled * zDistanceScaled >= 1.0D) {
                        continue;
                    }

                    for (int localY = endY; localY >= startY; localY--) {
                        double yDistanceScaled = (((double)localY + 0.5D) - currentBlockY) / caveRadiusVertical;
                        //yDistanceScaled > -0.7 ==> flattened floor
                        if (yDistanceScaled > -0.7D &&
                                xDistanceScaled * xDistanceScaled + yDistanceScaled * yDistanceScaled + zDistanceScaled * zDistanceScaled < 1.0D) {
                            int previousBlock = data.getType(localX, localY + 1, localZ);
                            if (previousBlock == 2) {
                                hitGrassSurface = true;
                            }
                            if (previousBlock == 1
                                    || previousBlock == 3
                                    || previousBlock == 2) {
                                if (localY < 10) {
                                    data.setBlock(localX, localY + 1, localZ, 10);
                                } else {
                                    data.setBlock(localX, localY + 1, localZ, 0);
                                    if (hitGrassSurface && data.getType(localX, localY, localZ) == 3) {
                                        data.setBlock(localX, localY , localZ, 2);
                                    }
                                }
                            }
                        }

                    }
                }
            }

            //why?
            if (noSplitBranch) {
                break;
            }
        }

    }

    //returns true of this distance can't be reached even after all remaining iterations
    private static boolean isCurrentChunkUnreachable(double distanceToOriginX, double distanceToOriginZ, int maxCaveSystemRadius, int currentCaveSystemRadius, float maxHorizontalSize) {
        double blocksLeft = maxCaveSystemRadius - currentCaveSystemRadius;
        //even if the exact block can't be reached, the chunk may be reachable by center of the cave
        //and cave size must be also included
        double bufferDistance = maxHorizontalSize + 2.0F + 16F;
        return (distanceToOriginX * distanceToOriginX + distanceToOriginZ * distanceToOriginZ) - blocksLeft * blocksLeft > bufferDistance * bufferDistance;
    }

    private boolean findWater(Chunk data, int startX, int endX, int startY, int endY, int startZ, int endZ) {
        for (int xPos = startX; xPos < endX; xPos++) {
            for (int zPos = startZ; zPos < endZ; zPos++) {
                for (int yPos = endY + 1; yPos >= startY - 1; yPos--) {
                    if (yPos < 0 || yPos >= 128) {
                        continue;
                    }
                    if (data.getType(xPos, yPos, zPos) == 9
                            || data.getType(xPos, yPos, zPos) == 8) {
                        return true;
                    }
                    if (yPos != startY - 1 && xPos != startX && xPos != endX - 1
                            && zPos != startZ && zPos != endZ - 1) {
                        yPos = startY;
                    }
                }

            }
        }
        return false;
    }

    public static class MathHelper {
        public static final float PI = 3.1415927F;
        private static float SIN_TABLE[];

        static {
            SIN_TABLE = new float[0x10000];
            for(int i = 0; i < 0x10000; i++) {
                SIN_TABLE[i] = (float)Math.sin((i * Math.PI * 2D) / 65536D);
            }
        }

        public static float sin(float f) {
            return SIN_TABLE[(int)(f * 10430.38F) & 0xffff];
        }

        public static float cos(float f) {
            return SIN_TABLE[(int)(f * 10430.38F + 16384F) & 0xffff];
        }

        public static float sqrt(float f) {
            return (float)Math.sqrt(f);
        }

        public static float sqrt(double d) {
            return (float)Math.sqrt(d);
        }

        public static int floor(float f) {
            int i = (int)f;
            return f >= (float)i ? i : i - 1;
        }

        public static int floor(double d) {
            int i = (int)d;
            return d >= (double)i ? i : i - 1;
        }

        public static float abs(float f) {
            return f < 0.0F ? -f : f;
        }

        public static double clamp(double f, double f1, double f2) {
            return f < f1 ? f1 : (f > f2 ? f2 : f);
        }
    }

}
