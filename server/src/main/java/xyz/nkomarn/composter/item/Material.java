package xyz.nkomarn.composter.item;

public enum Material {

    STONE(1, 0),
    GRASS_BLOCK(2, 0),
    DIRT(3, 0),
    COBBLESTONE(4, 0),
    PLANKS(5, 0),
    OAK_SAPLING(6, 0),
    SPRUCE_SAPLING(6, 1),
    BIRCH_SAPLING(6, 2),
    BEDROCK(7, 0);

    // TODO weird flowing/non-flowing water/lava

    private final int id, data;

    Material(int id, int data) {
        this.id = id;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public int getData() {
        return data;
    }
}
