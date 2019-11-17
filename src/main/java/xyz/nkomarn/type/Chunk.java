package xyz.nkomarn.type;

/**
 * Represents a chunk
 */
public class Chunk {

    private final int x, z;
    private final Block[] blocks;

    public Chunk(final int x, final int z) {
        this.x = x;
        this.z = z;
        this.blocks = new Block[32768];
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public Material getType(final int x, final int y, final int z) {
        return blocks[getIndex(x, y, z)].getType();
    }
    
    public void setBlocks(final Block[] blocks) {
        if (blocks.length != 32768) throw new IllegalArgumentException();
        System.arraycopy(blocks, 0, this.blocks, 0, blocks.length);
    }

    public int getMetaData(int x, int z, int y) {
        return blocks[getIndex(x, z, y)].getMetadata();
    }

    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException();

        this.blocks[getIndex(x, z, y)].setMetadata((byte) metaData);
    }

    public int getSkyLight(int x, int z, int y) {
        return 15; // todo
        //return skyLight[getIndex(x, z, y)];
    }

    public void setSkyLight(int x, int z, int y, int skyLight) {
        if (skyLight < 0 || skyLight >= 16)
            throw new IllegalArgumentException();

        //this.skyLight[getIndex(x, z, y)] = (byte) skyLight;
    }

    public int getBlockLight(int x, int z, int y) {
        return 15;
        //return blockLight[getIndex(x, z, y)];
    }

    public void setBlockLight(int x, int z, int y, int blockLight) {
        if (blockLight < 0 || blockLight >= 16)
            throw new IllegalArgumentException();

        //this.blockLight[getIndex(x, z, y)] = (byte) blockLight;
    }

    private int getIndex(final int x, final int y, final int z) {
        if (x < 0 || z < 0 || y < 0 || x >= 16 || z >= 16 || y >= 128)
            throw new IndexOutOfBoundsException();

        return (x * 16 + z) * 128 + y;
    }

}
