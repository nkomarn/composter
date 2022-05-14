package xyz.nkomarn.composter.type;

/**
 * Represents a block in a chunk
 */
public class Block {

    // TODO lighting (sky and block light as well as metadata)

    private final Chunk chunk;
    private final int x;
    private final int y;
    private final int z;
    private int type;
    private int metadata;
    private int blockLight;
    private int skyLight;

    public Block(final Chunk chunk, final int x, final int y,
                 final int z, final int type, final int metadata) {
        this.chunk = chunk;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
        this.metadata = metadata;
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

    public int getMetadata() {
        return this.metadata;
    }

    public void setMetadata(final int metadata) {
        this.metadata = metadata;
    }

    public int getType() {
        return this.type;
    }

    public Chunk getChunk() {
        return this.chunk;
    }

}
