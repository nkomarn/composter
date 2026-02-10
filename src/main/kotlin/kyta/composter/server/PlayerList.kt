package kyta.composter.server

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
import kyta.composter.world.entity.Player
import kyta.composter.world.entity.pos
import java.util.*

class PlayerList(private val server: MinecraftServer) : Tickable, Iterable<Player> {
    private val onlinePlayers = mutableMapOf<String, Player>()
    private val chatLogger = LoggerFactory.getLogger("chat")

    fun getPlayer(username: String): Player? {
        return onlinePlayers[username.lowercase(Locale.getDefault())]
    }

    fun broadcastMessage(message: Component) {
        broadcastPacket(ClientboundChatMessagePacket(message))
        chatLogger.info(PlainTextComponentSerializer.plainText().serialize(message))
    }

    fun broadcastPacket(packet: Packet) {
        forEach { it.connection.sendPacket(packet) }
    }

    fun playerJoined(player: Player) {
        val username = player.username
        onlinePlayers[username.lowercase()] = player

        /* add the player into the world */
        val worldSpawn = player.world.properties.spawn
        player.pos = Vec3d(worldSpawn).add(0.0, Player.EYE_HEIGHT, 0.0)

        player.updateVisibleChunks()
        player.menuSynchronizer.synchronize()
        player.connection.sendPacket(ClientboundSetSpawnPacket(worldSpawn))
        player.world.entities.add(player)

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

    override fun iterator(): Iterator<Player> {
        return onlinePlayers.values.iterator()
    }

    override fun tick(currentTick: Long) {
        if (currentTick % 300 == 0L) {
            onlinePlayers.values.forEach {
                it.connection.sendPacket(GenericKeepAlivePacket)
            }
        }
    }
}
