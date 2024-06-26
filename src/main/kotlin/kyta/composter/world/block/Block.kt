package kyta.composter.world.block

data class Block(val networkId: Int)

data class BlockState(
    val block: Block,
    val metadataValue: Int,
)