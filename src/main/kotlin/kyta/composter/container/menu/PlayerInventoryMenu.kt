package kyta.composter.container.menu

import kyta.composter.container.BasicContainer
import kyta.composter.container.Container
import kyta.composter.protocol.packet.play.ServerboundMenuInteractionPacket
import xyz.nkomarn.composter.entity.Player

interface Menu {
    val slots: List<Slot>
    val currentStateId: Int

    fun getSlot(networkId: Int): Slot?
    fun addSlot(slot: Slot)

    fun interact(player: Player, packet: ServerboundMenuInteractionPacket)
    fun incrementState(): Int
}

abstract class BasicMenu: Menu {
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

        /* swap cursor items on normal click */
        player.cursorItem = slot.item
        slot.item = player.cursorItem
    }

    override fun incrementState(): Int {
        return ++_state
    }
}

class PlayerInventoryMenu(
    inventory: Container,
    armor: Container,
) : BasicMenu() {
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