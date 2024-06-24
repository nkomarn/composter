package kyta.composter.container.menu

import kyta.composter.container.Container
import kyta.composter.item.ItemStack

data class Slot(
    val networkIndex: Int,
    val containerIndex: Int,
    val backingContainer: Container,
) {
    var item: ItemStack
        get() = backingContainer.getItem(containerIndex)
        set(value) = backingContainer.setItem(containerIndex, value)
}