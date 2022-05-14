package xyz.nkomarn.composter.type;

import java.util.Arrays;

/**
 * Represents a chunk
 */
public class Chunk {

    private final int x, z;
    private final byte[] blocks, metadata, skyLight, blockLight;
    private final int arraySize = 32768;
    private int lastX, lastY, lastZ;

    public Chunk(final int x, final int z) {
        this.x = x;
        this.z = z;
        this.blocks = new byte[16 * 16 * 128];
        this.metadata = new byte[arraySize];
        this.skyLight = new byte[arraySize];
        this.blockLight = new byte[arraySize];

        //TODO temp test so i dont have to deal with lighting
        Arrays.fill(skyLight, (byte) 15);
        Arrays.fill(blockLight, (byte) 15);
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

    public void setBlock(final int x, final int y, final int z, final int type) {
        if (type < 0 || type >= 97)
            throw new IllegalArgumentException("Illegal block ID.");
        this.blocks[getIndex(x, y, z)] = (byte) type;
    }

    public void setBlocks(final byte[] blocks) {
        if (blocks.length != arraySize) throw new IllegalArgumentException();
        System.arraycopy(blocks, 0, this.blocks, 0, blocks.length);
    }

    public int getMetaData(int x, int z, int y) {
        //return metadata[getIndex(x, y, z)];
        return 0;
    }

    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException();
        //this.metadata[getIndex(x, y, z)] = (byte) metaData;
        return;
    }

    public int getSkyLight(int x, int z, int y) {
        //return skyLight[getIndex(x, y, z)];
        return 15;
    }

    public void setLastBlock(int x, int y, int z){
        lastX = x; lastY = y; lastZ = z;
    }

    public int[] getLastBlock(){
        return new int[]{lastX, lastY, lastZ};
    }

    public void setSkyLight(int x, int z, int y, int skyLight) {
        if (skyLight < 0 || skyLight >= 16)
            throw new IllegalArgumentException();
        //this.skyLight[getIndex(x, y, z)] = (byte) skyLight;
    }

    public int getBlockLight(int x, int z, int y) {
        //return blockLight[getIndex(x, y, z)];
        return 15;
    }

    public void setBlockLight(int x, int z, int y, int blockLight) {
        if (blockLight < 0 || blockLight >= 16)
            throw new IllegalArgumentException();

        //this.blockLight[getIndex(x, y, z)] = (byte) blockLight;
    }

    private int getIndex(final int x, final int y, final int z) {
        /*if (x < 0 || z < 0 || y < 0 || x >= 16 || z >= 16 || y >= 128)
            throw new IndexOutOfBoundsException();*/
        return (Math.min(16, x) * 16 + Math.min(16, z)) * 128 + Math.min(128, y);
    }

    public boolean isEmpty(int blockX, int blockY, int blockZ) {
        if(getType(blockX, blockY, blockZ) == 0){
            return true;
        }else{
            return false;
        }
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

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + x;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Key other = (Key) obj;
            if (x != other.x)
                return false;
            if (z != other.z)
                return false;
            return true;
        }
    }

    // Serialize all the blocks in the chunk so we can send them to the client
    public byte[] serializeTileData() {
        byte[] dest = new byte[((arraySize * 5) / 2)];

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
