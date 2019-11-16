package xyz.nkomarn.model;

public class Chunk {

    private final int x, z;

    public Chunk(final int x, final int z) {
        this.x = x;
        this.z = z;
    }

    public Block getBlockAt(final int x, final int y, final int z) {
        // TODO return block object
        return null;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

}
