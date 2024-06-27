package kyta.composter.world.block

data class Block(val networkId: Int)

data class BlockState(
    val block: Block,
    val metadataValue: Int,
)

fun BlockState.isAir(): Boolean {
    return block == AIR // todo; change to direct object check once a registry is implemented
}