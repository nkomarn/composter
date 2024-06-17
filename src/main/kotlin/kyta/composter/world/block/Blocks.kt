package kyta.composter.world.block

val AIR = register(Block(0))
val STONE = register(Block(1))
val GRASS_BLOCK = register(Block(2))
val DIRT = register(Block(3))
val COBBLESTONE = register(Block(4))
val WOODEN_PLANKS = register(Block(5))
val SAPLING = register(Block(6))
val BEDROCK = register(Block(7))

val Block.defaultState
    get() = BlockState(this, 0)

private fun <T : Block?> register(block: T): T {
    return block
}