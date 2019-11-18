package xyz.nkomarn.type;

/**
 * Represents a chunk
 */
public class Chunk {

    private final int x, z;
    private final byte[] blocks, metadata, skyLight, blockLight;

    public Chunk(final int x, final int z) {
        this.x = x;
        this.z = z;
        this.blocks = new byte[32768];
        this.metadata = new byte[32768];
        this.skyLight = new byte[32768];
        this.blockLight = new byte[32768];
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public int getType(final int x, final int y, final int z) {
        return blocks[getIndex(x, y, z)];
    }

    public void setBlock(final int x, final int y, final int z, final byte block) {
        this.blocks[getIndex(x, z, y)] = block;
    }

    public void setBlocks(final byte[] blocks) {
        if (blocks.length != 32768) throw new IllegalArgumentException();
        System.arraycopy(blocks, 0, this.blocks, 0, blocks.length);
    }

    public int getMetaData(int x, int z, int y) {
        return metadata[getIndex(x, z, y)];
    }

    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException();
        this.metadata[getIndex(x, z, y)] = (byte) metaData;
    }

    public int getSkyLight(int x, int z, int y) {
        return skyLight[getIndex(x, z, y)];
    }

    public void setSkyLight(int x, int z, int y, int skyLight) {
        if (skyLight < 0 || skyLight >= 16)
            throw new IllegalArgumentException();
        this.skyLight[getIndex(x, z, y)] = (byte) skyLight;
    }

    public int getBlockLight(int x, int z, int y) {
        return blockLight[getIndex(x, z, y)];
    }

    public void setBlockLight(int x, int z, int y, int blockLight) {
        if (blockLight < 0 || blockLight >= 16)
            throw new IllegalArgumentException();
        this.blockLight[getIndex(x, z, y)] = (byte) blockLight;
    }

    private int getIndex(final int x, final int y, final int z) {
        if (x < 0 || z < 0 || y < 0 || x >= 16 || z >= 16 || y >= 128)
            throw new IndexOutOfBoundsException();
        return (x * 16 + z) * 128 + y;
    }

    // Used for getting chunk by coordinates in HashMap
    public static final class Key {
        private final int x, z;

        public Key(int x, int z) {
            this.x = x;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getZ() {
            return z;
        }
    }

    // Serialize all the blocks in the chunk so we can send them to the client
    public byte[] serializeTileData() {
        byte[] dest = new byte[((16 * 16 * 128 * 5) / 2)];

        System.arraycopy(blocks, 0, dest, 0, blocks.length);

        int pos = blocks.length;

        for (int i = 0; i < metadata.length; i += 2) {
            byte meta1 = metadata[i];
            byte meta2 = metadata[i + 1];
            dest[pos++] = (byte) ((meta2 << 4) | meta1);
        }

        for (int i = 0; i < skyLight.length; i += 2) {
            byte light1 = skyLight[i];
            byte light2 = skyLight[i + 1];
            dest[pos++] = (byte) ((light2 << 4) | light1);
        }

        for (int i = 0; i < blockLight.length; i += 2) {
            byte light1 = blockLight[i];
            byte light2 = blockLight[i + 1];
            dest[pos++] = (byte) ((light2 << 4) | light1);
        }

        return dest;
    }
}
