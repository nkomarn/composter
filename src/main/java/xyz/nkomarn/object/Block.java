package xyz.nkomarn.object;

/**
 * Represents a block in a chunk
 */
public class Block {

    private final Chunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private final Material type;

    public Block(final Chunk chunk, final int x, final int y,
                 final int z, final Material type) {
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public Material getType() {
        return this.type;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

}
