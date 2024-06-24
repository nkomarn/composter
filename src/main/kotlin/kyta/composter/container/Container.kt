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

    /* returns the remainder */
    fun addItem(stack: ItemStack): Int
}

open class BasicContainer(override val size: Int) : Container {
    protected open val contents = arrayOfNulls<ItemStack>(size)
    override val items = contents.asSequence().map { it ?: ItemStack.EMPTY }
    override var dirty = false

    override fun getItem(slot: Int): ItemStack {
        check(slot in 0..<size)
        return contents[slot] ?: ItemStack.EMPTY
    }

    override fun setItem(slot: Int, item: ItemStack) {
        check(slot in 0..<size)
        contents[slot] = item
        dirty = true
    }

    override fun addItem(stack: ItemStack): Int {
        /**
         * order of operations:
         * - merge the item into an existing, similar stack
         * - add the item to an available empty slot
         */
        dirty = true

        for (other in items) {
            stack.mergeInto(other)

            if (stack.isEmpty) {
                return 0
            }
        }

        for ((slot, other) in contents.withIndex()) {
            if (other != null) continue
            setItem(slot, stack.clone())
            stack.count = 0
            break
        }

        return stack.count
    }
}