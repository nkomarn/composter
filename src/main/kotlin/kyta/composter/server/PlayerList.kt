package kyta.composter.server

import kyta.composter.Tickable
import kyta.composter.math.Vec3d
import kyta.composter.protocol.Packet
import kyta.composter.protocol.packet.GenericKeepAlivePacket
import kyta.composter.protocol.packet.play.ClientboundChatMessagePacket
import kyta.composter.protocol.packet.play.ClientboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.ClientboundSetSpawnPacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.slf4j.LoggerFactory
import xyz.nkomarn.composter.entity.Player
import java.util.*

class PlayerList(private val server: MinecraftServer) : Tickable {
    private val onlinePlayers = mutableMapOf<String, Player>()
    private val chatLogger = LoggerFactory.getLogger("chat")

    fun getPlayer(username: String): Player? {
        return onlinePlayers[username.lowercase(Locale.getDefault())]
    }

    fun onlinePlayers(): Collection<Player> {
        return Collections.unmodifiableCollection(onlinePlayers.values)
    }

    fun broadcastMessage(message: Component) {
        broadcastPacket(ClientboundChatMessagePacket(message))
        chatLogger.info(PlainTextComponentSerializer.plainText().serialize(message))
    }

    fun broadcastPacket(packet: Packet) {
        onlinePlayers().forEach { it.connection.sendPacket(packet) }
    }

    fun playerJoined(player: Player) {
        val username = player.username
        onlinePlayers[username.lowercase()] = player

        /* add the player into the world */
        val worldSpawn = player.world.properties.spawn.up(3)
        player.pos = Vec3d(worldSpawn)
        player.world.addEntity(player)

//         player.updateVisibleChunks()
        player.connection.sendPacket(ClientboundSetSpawnPacket(worldSpawn))

        /* send the player's spawning position */
        player.connection.sendPacket(
            ClientboundSetAbsolutePlayerPositionPacket(
                player.pos,
                player.stance,
                player.yaw,
                player.pitch,
                player.isOnGround,
            )
        )

        broadcastMessage(Component.text(player.username + " joined the game.", NamedTextColor.YELLOW))
    }

    fun playerDisconnected(player: Player) {
        player.markRemoved()

        val username = player.username
        onlinePlayers.remove(username.lowercase())
        broadcastMessage(Component.text(player.username + " left the game.", NamedTextColor.YELLOW))
    }

    override fun tick(currentTick: Long) {
        if (currentTick % 300 == 0L) {
            onlinePlayers.values.forEach {
                it.connection.sendPacket(GenericKeepAlivePacket)
            }
        }
    }
}
