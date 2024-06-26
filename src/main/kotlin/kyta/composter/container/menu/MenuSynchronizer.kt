package kyta.composter.container.menu

import kyta.composter.server.Tickable
import kyta.composter.protocol.packet.play.ClientboundSetContainerContentPacket
import kyta.composter.world.entity.Player

class MenuSynchronizer(private val player: Player) : Tickable {
    private val inventoryMenu = PlayerInventoryMenu(player, player.inventory, player.armorContainer)

    var currentMenu: Menu = inventoryMenu
        private set

    var counter = 0
        private set

    fun openMenu(menu: Menu) {
        /* todo; implement menu types + "open" packet
        currentMenu = menu
        player.connection.sendPacket(ClientboundSetContainerContentPacket(nextId(), menu))
         */
    }

    fun closeMenu(id: Int) {
        if (counter == id) {
            currentMenu = inventoryMenu
        }
    }

    /**
     * The menu counter must be serializable as a byte;
     * roll over to 1 if the value goes over the maximum
     * representable byte value.
     */
    fun nextId(): Int {
        counter = (counter + 1) % Byte.MAX_VALUE
        return counter
    }

    fun synchronize() {
        player.connection.sendPacket(ClientboundSetContainerContentPacket(currentMenu.id, currentMenu))
    }

    override fun tick(currentTick: Long) {
        currentMenu.tick(currentTick)
    }
}