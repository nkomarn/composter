package kyta.composter.world.block

val AIR = register(Block(0))
val STONE = register(Block(1))
val GRASS_BLOCK = register(Block(2))
val DIRT = register(Block(3))
val COBBLESTONE = register(Block(4))
val WOODEN_PLANKS = register(Block(5))
val SAPLING = register(Block(6))
val BEDROCK = register(Block(7))
val FLOWING_WATER = register(Block(8))
val WATER = register(Block(9))
val SAND = register(Block(12))
val GRAVEL = register(Block(13))
val SANDSTONE = register(Block(24))
val CACTUS = register(Block(81))
val SHORT_GRASS = register(Block(31))
val DANDELION = register(Block(37))

val Block.defaultState
    get() = BlockState(this, 0)

private fun <T : Block?> register(block: T): T {
    return block
}