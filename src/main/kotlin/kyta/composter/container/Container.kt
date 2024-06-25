package kyta.composter.container

import kyta.composter.item.ItemStack
import kyta.composter.item.isEmpty
import kyta.composter.item.mergeInto

interface Container {
    val size: Int
    val items: Sequence<ItemStack>
    var dirty: Boolean

    fun getItem(slot: Int): ItemStack
    fun setItem(slot: Int, stack: ItemStack)

    /**
     * attempts to insert the given item into the inventory.
     * any remaining part of the stack which could not be
     * inserted is returned.
     */
    fun insert(stack: ItemStack): ItemStack
}

open class BasicContainer(override val size: Int) : Container {
    protected open val contents = arrayOfNulls<ItemStack>(size)
    override val items = contents.asSequence().map { it ?: ItemStack.EMPTY }
    override var dirty = false

    override fun getItem(slot: Int): ItemStack {
        check(slot in 0..<size)
        return contents[slot] ?: ItemStack.EMPTY
    }

    override fun setItem(slot: Int, stack: ItemStack) {
        check(slot in 0..<size)
        contents[slot] = stack.takeUnless { it.isEmpty }
        dirty = true
    }

    /**
     * the order of operations:
     * - attempt to merge into an existing, non-empty but similar stack
     * - add the item to the next available empty slot
     * - give up and return the item as a remainder
     */
    override fun insert(stack: ItemStack): ItemStack {
        var remainder = stack

        for ((index, other) in contents.withIndex()) {
            if (other == null) continue
            val result = remainder.mergeInto(other)
                ?: continue // the merge is not possible

            setItem(index, result.second)
            remainder = result.first

            /* stop here if the remainder is empty */
            if (remainder.isEmpty) {
                return remainder
            }
        }

        /**
         * if we still haven't completely disposed of the
         * item, try to add it to the next empty slot in
         * the inventory.
         */
        for ((index, other) in contents.withIndex()) {
            if (other != null) continue

            setItem(index, remainder)
            remainder = ItemStack.EMPTY
            break
        }

        return remainder
    }
}