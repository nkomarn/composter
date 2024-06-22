package kyta.composter.container

import kyta.composter.item.ItemStack

interface Container {
    val size: Int
    val items: Sequence<ItemStack>
    var dirty: Boolean

    fun getItem(slot: Int): ItemStack
    fun setItem(slot: Int, stack: ItemStack)

    /* returns the remainder */
    fun addItem(stack: ItemStack): Int
}

open class BasicContainer(override val size: Int): Container {
    private val contents = arrayOfNulls<ItemStack>(size)
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
        return 0 // todo
    }
}