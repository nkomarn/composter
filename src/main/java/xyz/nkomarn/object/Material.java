package xyz.nkomarn.object;

public enum Material {
    AIR(0, 0),
    STONE(1, 0),
    GRASS_BLOCK(2,0),
    DIRT(3, 0),
    COBBLESTONE(4, 0),
    PLANKS(5, 0),
    OAK_SAPLING(6, 0),
    SPRUCE_SAPLING(6, 1),
    BIRCH_SAPLING(6, 2),
    BEDROCK(7, 0), // TODO WATER AND LAVA
    SAND(12, 0),
    GRAVEL(13, 0),
    GOLD_ORE(14, 0),
    IRON_ORE(15, 0),
    COAL_ORE(16, 0);

    private int id;
    private int metadata;

    public int getId() {
        return this.id;
    }

    public int getMetadata() {
        return this.metadata;
    }

    private Material(final int id, final int metadata) {
        this.id = id;
        this.metadata = metadata;
    }
}
