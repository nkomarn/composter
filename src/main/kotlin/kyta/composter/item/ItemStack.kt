package kyta.composter.item

data class ItemStack(val id: Int, val metadata: Int) {
    companion object {
        val EMPTY = ItemStack(0, 0)
    }
}
