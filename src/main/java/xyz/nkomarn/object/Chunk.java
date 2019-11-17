package xyz.nkomarn.object;

/**
 * Represents a chunk
 */
public class Chunk {

    private final int x, z;
    private final Block[][][] blocks =
        new Block[16][16][128]; // TODO better block storage

    public Chunk(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    // TODO possibly remove
    public void setBlock(final int x, final int y, final int z, Block block) {
        blocks[x][y][z] = block;
    }

    public void setBlock(Block block) {
        blocks[block.getX()][block.getY()][block.getZ()] = block;
    }

    public Block getBlockAt(final int x, final int y, final int z) {
        return blocks[x][y][z];
    }

    public Block[][][] getBlocks() {
        return this.blocks;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

}
