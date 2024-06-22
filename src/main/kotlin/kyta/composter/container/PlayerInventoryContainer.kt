package kyta.composter.container

import kyta.composter.item.ItemStack

class PlayerInventoryContainer : BasicContainer(36) {
    private val armorContents = arrayOfNulls<ItemStack>(size)
}