package kyta.composter.container.menu

import kyta.composter.Tickable
import kyta.composter.container.BasicContainer
import kyta.composter.container.Container
import kyta.composter.item.grow
import kyta.composter.item.isEmpty
import kyta.composter.item.isSimilarTo
import kyta.composter.item.shrink
import kyta.composter.item.split
import kyta.composter.protocol.packet.play.ServerboundMenuInteractionPacket
import xyz.nkomarn.composter.entity.Player
import kotlin.math.ceil

interface Menu : Tickable {
    val viewer: Player
    val slots: List<Slot>
    val currentStateId: Int

    fun getSlot(networkId: Int): Slot?
    fun addSlot(slot: Slot)

    fun interact(player: Player, packet: ServerboundMenuInteractionPacket)
    fun incrementState(): Int
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
        val slot = getSlot(packet.slot)
            ?: return // todo; probably need to re-synchronize the menu

        if (packet.rightClicked) {
            if (slot.item.isEmpty) {
                if (!player.cursorItem.isEmpty) {
                    /**
                     * drop one item into the slot if the slot is empty
                     * and the player is hovering with an item.
                     */
                    val (old, new) = player.cursorItem.split(1)
                    player.cursorItem = old
                    slot.item = new
                }
            } else {
                if (player.cursorItem.isEmpty) {
                    /**
                     * split the stack in half, leaving the rounded down value in the
                     * slot. set the new stack as the player's hover item.
                     */
                    val (old, new) = slot.item.split(ceil(slot.item.count / 2.0).toInt())
                    slot.item = old
                    player.cursorItem = new
                } else {
                    if (player.cursorItem.isSimilarTo(slot.item)) {
                        /**
                         * add one to the existing stack, subtract one from cursor.
                         */
                        player.cursorItem = player.cursorItem.shrink(1)
                        slot.item = slot.item.grow(1)
                    } else {
                        /**
                         * basically normal non-right-click action
                         * (swap items).
                         */
                        val cursorItem = player.cursorItem
                        player.cursorItem = slot.item
                        slot.item = cursorItem
                    }
                }
            }
        } else {
            /**
             * normal click actions.
             */

            /* swap cursor items on normal click */
            val cursorItem = player.cursorItem
            player.cursorItem = slot.item
            slot.item = cursorItem
        }
    }

    override fun incrementState(): Int {
        return ++_state
    }

    override fun tick(currentTick: Long) {
        /**
         * send menu updates if any slot is marked dirty.
         */
        if (slots.any { it.backingContainer.dirty }) {
//             viewer.connection.sendPacket(ClientboundSetContainerContentPacket(viewer.menuCounter, this))
            incrementState()
            slots.forEach { it.backingContainer.dirty = false }
        }
    }
}

class PlayerInventoryMenu(
    viewer: Player,
    inventory: Container,
    armor: Container,
) : BasicMenu(viewer) {
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