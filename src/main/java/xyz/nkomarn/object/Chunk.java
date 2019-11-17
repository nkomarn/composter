package xyz.nkomarn.object;

public class Chunk {

    private final int x, z;
    private final int[][][] blocks =
        new int[16][16][128]; // TODO better block storage

    public Chunk(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    public void setBlock(final int x, final int y, final int z, int block) {
        blocks[x][y][z] = block;
    }

    public int getBlockAt(final int x, final int y, final int z) {
        return blocks[x][y][z]; // TODO return block object later
    }

    public int[][][] getBlocks() {
        return this.blocks;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

}
