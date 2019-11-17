package xyz.nkomarn.type;

public enum Material {
    AIR(0),
    STONE(1),
    GRASS_BLOCK(2),
    DIRT(3),
    COBBLESTONE(4),
    PLANKS(5),
    SAPLING(6),
    BEDROCK(7), // TODO WATER AND LAVA
    SAND(12),
    GRAVEL(13),
    GOLD_ORE(14),
    IRON_ORE(15),
    COAL_ORE(16);

    private int id;

    public int getId() {
        return this.id;
    }

    private Material(final int id) {
        this.id = id;
    }
}
