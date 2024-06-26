package kyta.composter.item

import kyta.composter.world.block.AIR
import kotlin.math.min

data class Item(
    val networkId: Int,
    val maxStackSize: Int = 64,
    val metadataValue: Int = 0,
)

data class ItemStack(
    val item: Item,
    val count: Int = 1,
    val metadataValue: Int = item.metadataValue,
) {
    companion object {
        val EMPTY = ItemStack(Item(0), 0, 0)
    }
}

/* immutable cloning methods */
fun ItemStack.withCount(count: Int): ItemStack {
    if (count == 0) return ItemStack.EMPTY
    return ItemStack(this.item, count.coerceIn(0, this.item.maxStackSize), this.metadataValue)
}

fun ItemStack.withMetadataValue(value: Int): ItemStack {
    return ItemStack(this.item, this.count, value)
}

fun ItemStack.clone(): ItemStack {
    return ItemStack(this.item, this.count, this.metadataValue)
}

val ItemStack.isEmpty: Boolean
    get() = count == 0 || item.networkId == AIR.networkId

fun ItemStack.shrink(count: Int = 1): ItemStack {
    return withCount((this.count - count).coerceIn(0, item.maxStackSize))
}

fun ItemStack.grow(count: Int = 1): ItemStack {
    return withCount((this.count + count).coerceIn(0, item.maxStackSize))
}

/**
 * returns two stacks:
 * 1.) the old stack with items removed
 * 2.) the newly created stack with the specified count
 */
fun ItemStack.split(count: Int): Pair<ItemStack, ItemStack> {
    return shrink(count) to withCount(count)
}

/* returns the remaining count */

/**
 * returns two stacks:
 * 1.) the old stack which was merged with any excess amount remaining
 * 2.) a new stack which was merged into
 *
 * or: null if the merge is not possible due to a lack in similarity
 */
fun ItemStack.mergeInto(other: ItemStack): Pair<ItemStack, ItemStack>? {
    if (!this.isSimilarTo(other)) return null

    val remainingSpace = other.item.maxStackSize - other.count
    val availableToMove = min(this.count, remainingSpace)

    return this.shrink(availableToMove) to other.grow(availableToMove)
}

fun ItemStack.isSimilarTo(stack: ItemStack): Boolean {
    return this.item.networkId == stack.item.networkId && stack.metadataValue == metadataValue
}

fun ItemStack.canFit(count: Int = 1) : Boolean {
    return this.count + count <= item.maxStackSize
}