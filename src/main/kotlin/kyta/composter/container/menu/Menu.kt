package kyta.composter.container.menu

import kyta.composter.Tickable
import kyta.composter.container.BasicContainer
import kyta.composter.container.Container
import kyta.composter.item.isEmpty
import kyta.composter.item.isSimilarTo
import kyta.composter.item.shrink
import kyta.composter.item.split
import kyta.composter.protocol.packet.play.ServerboundMenuInteractionPacket
import xyz.nkomarn.composter.entity.Player
import kotlin.math.ceil
import kyta.composter.item.ItemStack
import kyta.composter.item.canFit
import kyta.composter.item.mergeInto
import kyta.composter.item.withCount
import xyz.nkomarn.composter.entity.drop

interface Menu : Tickable {
    val id: Int
    val viewer: Player
    val slots: List<Slot>
    val currentStateId: Int

    fun getSlot(networkId: Int): Slot?
    fun addSlot(slot: Slot)

    fun interact(player: Player, packet: ServerboundMenuInteractionPacket)
    fun incrementState(): Int
}

private fun getSlotAction(storedItem: ItemStack, cursorItem: ItemStack, rightClicked: Boolean): SlotAction? {
    /**
     * left-click container actions:
     * - if the items are the same, place as much as possible into the slot
     * - otherwise, swap the item in the slot with the cursor item
     */
    if (!rightClicked) {
        return if (cursorItem.isSimilarTo(storedItem)) {
            SlotAction.PLACE_STACK
        } else {
            SlotAction.SWAP_WITH_CURSOR
        }
    }

    /**
     * right-click container actions:
     * - if the slot is empty and there is a cursor item, place one of the item
     *   into the slot
     * - if the slot is filled but the cursor is empty, take half (rounded up)
     *   from the slot
     * - if the slot is filled and the cursor item is the same as the slot item,
     *   place one of the item into the slot
     * - otherwise, swap the item in the slot with the cursor item
     */
    if (storedItem.isEmpty) {
        if (storedItem.isEmpty && !cursorItem.isEmpty) {
            return SlotAction.PLACE_ONE
        }
    } else {
        return if (cursorItem.isEmpty) {
            SlotAction.TAKE_HALF
        } else {
            if (cursorItem.isSimilarTo(storedItem)) {
                if (cursorItem.canFit(1)) {
                    println("place 1, can fit 1")
                    SlotAction.PLACE_ONE
                } else {
                    null
                }
            } else {
                SlotAction.SWAP_WITH_CURSOR
            }
        }
    }

    return null
}

enum class SlotAction {
    TAKE_HALF,
    PLACE_ONE,
    PLACE_STACK,
    SWAP_WITH_CURSOR,
}

open class BaseMenu {
    val slots = mutableListOf<Slot>()
    var state = 0
        private set

    fun incrementState(): Int {
        return ++state
    }
}


abstract class BasicMenu(override val viewer: Player) : Menu {
    private val _slots = mutableMapOf<Int, Slot>()
    override val slots: List<Slot>
        get() = _slots.values.sortedBy { it.networkIndex }

    private var _state = 0
    override val currentStateId: Int
        get() = _state

    override fun getSlot(networkId: Int): Slot? {
        return _slots[networkId]
    }

    override fun addSlot(slot: Slot) {
        _slots[slot.networkIndex] = slot
    }

    override fun interact(player: Player, packet: ServerboundMenuInteractionPacket) {
        if (packet.slot == -1) {
            /**
             * special case: drop the currently held cursor item.
             */
            player.cursorItem.takeUnless(ItemStack::isEmpty)
                ?.let { player.drop(it) }

            return
        }

        val slot = getSlot(packet.slot)
            ?: return player.menuSynchronizer.synchronize()

        val cursorItem = player.cursorItem
        val action = getSlotAction(slot.item, cursorItem, packet.rightClicked)

        println(action)

        when (action) {
            SlotAction.TAKE_HALF -> {
                val (remainder, largerStack) = slot.item.split(ceil(slot.item.count / 2.0).toInt())
                slot.item = remainder
                player.cursorItem = largerStack
            }

            SlotAction.PLACE_ONE -> {
                player.cursorItem = cursorItem.shrink()
                slot.item = cursorItem.withCount(slot.item.count + 1)
            }

            SlotAction.PLACE_STACK -> {
                val (remaining, updatedStack) = cursorItem.mergeInto(slot.item)
                    ?: throw IllegalStateException()

                slot.item = updatedStack
                player.cursorItem = remaining
            }

            SlotAction.SWAP_WITH_CURSOR -> {
                player.cursorItem = slot.item
                slot.item = cursorItem
            }

            null -> throw IllegalStateException(packet.toString())
        }
    }

    override fun incrementState(): Int {
        return ++_state
    }

    override fun tick(currentTick: Long) {
        /**
         * send menu updates if any slot is marked dirty.
         */
        /*
        if (slots.any { it.backingContainer.dirty }) {
            viewer.sendMessage(Component.text("dirty container"))
            viewer.connection.sendPacket(ClientboundSetContainerContentPacket(viewer.menuCounter, this))
            slots.forEach { it.backingContainer.dirty = false }
        }
         */
    }
}

class PlayerInventoryMenu(
    viewer: Player,
    inventory: Container,
    armor: Container,
) : BasicMenu(viewer) {
    override val id = 0

    init {
        /* crafting slots */
        for (index in 0 until 5) {
            addSlot(Slot(index, 0, BasicContainer(1)))
        }

        /* armor slots */
        for (index in 0 until 4) {
            addSlot(Slot(index + 5, index, armor))
        }

        /* hotbar slots */
        for (index in 0 until 9) {
            addSlot(Slot(index + 36, index, inventory))
        }

        /* main inventory slots */
        for (index in 9 until 36) {
            addSlot(Slot(index, index, inventory))
        }
    }
}