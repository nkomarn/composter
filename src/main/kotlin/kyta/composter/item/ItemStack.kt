package kyta.composter.item

import kyta.composter.world.block.AIR
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

data class ItemStack(
    val id: Int,
    var count: Int,
    var metadataValue: Int,
    val maxStackSize: Int = 64,
) {
    fun clone(): ItemStack {
        return ItemStack(id, count, metadataValue, maxStackSize)
    }

    companion object {
        val EMPTY = ItemStack(0, 0, 0)
    }
}

val ItemStack.isEmpty: Boolean
    get() = id == AIR.id || count == 0

fun ItemStack.shrink(count: Int = 1) {
    this@shrink.count = max(0, this@shrink.count - count)
}

fun ItemStack.grow(count: Int = 1) {
    this@grow.count = min(maxStackSize, this@grow.count + count)
}

fun ItemStack.split(count: Int): ItemStack {
    this.shrink(count)
    return ItemStack(this.id, max(this.count, count), this.metadataValue)
}

/* returns the remaining count */
fun ItemStack.mergeInto(other: ItemStack) {
    if (this.matches(other)) {
        val remainingSpace = other.maxStackSize - other.count
        val availableToMove = min(this.count, remainingSpace)

        this.shrink(availableToMove)
        other.grow(availableToMove)
    }
}

fun ItemStack.matches(stack: ItemStack): Boolean {
    return stack.id == id && stack.metadataValue == metadataValue
}
