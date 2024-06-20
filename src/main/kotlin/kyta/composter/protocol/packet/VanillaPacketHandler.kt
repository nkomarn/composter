package kyta.composter.protocol.packet

import kyta.composter.network.Connection
import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.packet.handshaking.ClientboundHandshakePacket
import kyta.composter.protocol.packet.handshaking.ServerboundHandshakePacket
import kyta.composter.protocol.packet.login.ClientboundLoginPacket
import kyta.composter.protocol.packet.login.ServerboundLoginPacket
import kyta.composter.protocol.packet.play.ClientboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.FlyingStatusPacket
import kyta.composter.protocol.packet.play.PositionPacket
import kyta.composter.protocol.packet.play.RotationPacket
import kyta.composter.protocol.packet.play.ServerboundChatMessagePacket
import kyta.composter.protocol.packet.play.ServerboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.ServerboundSetPlayerFlyingStatusPacket
import kyta.composter.protocol.packet.play.ServerboundSetPlayerPositionPacket
import kyta.composter.protocol.packet.play.ServerboundSetPlayerRotationPacket
import kyta.composter.server.MinecraftServer
import kyta.composter.withContext
import kyta.composter.world.BlockPos
import kyta.composter.world.ChunkPos
import kyta.composter.world.dimension.DimensionType
import net.kyori.adventure.text.Component
import xyz.nkomarn.composter.entity.Player
import java.util.concurrent.ThreadLocalRandom

class VanillaPacketHandler(
    private val server: MinecraftServer,
    private val connection: Connection,
) : PacketHandler {
    override suspend fun handleHandshake(packet: ServerboundHandshakePacket) {
        connection.sendPacket(ClientboundHandshakePacket("-"))
        connection.state = ConnectionState.LOGIN
    }

    override suspend fun handleKeepAlive(packet: GenericKeepAlivePacket) {
        /**
         * todo; we'll want to implement a 1,200 tick timeout
         * rather than mirroring the packet.
         */
//         connection.sendPacket(GenericKeepAlivePacket())
    }

    override suspend fun handleLogin(packet: ServerboundLoginPacket) {
        if (connection.state != ConnectionState.LOGIN) {
            return connection.disconnect("Invalid connection state (${connection.state.name})")
        }

        if (packet.protocolVersion != 14) {
            return connection.disconnect("Unsupported protocol version (${packet.protocolVersion})")
        }

        /* disconnect existing players with the same username */
        withContext(server) {
            server.playerList.getPlayer(packet.username)
                ?.connection
                ?.disconnect("You logged in from another location")

            val player = Player(
                server.worldManager.primaryWorld(),
                connection,
                packet.username,
            )

            /* acknowledge login, advance connection status */
            connection.sendPacket(
                ClientboundLoginPacket(
                    player.id,
                    "composter",
                    ThreadLocalRandom.current().nextLong(),
                    DimensionType.OVERWORLD,
                )
            )
            connection.player = player
            connection.state = ConnectionState.PLAY

            /* finish up the rest of the joining flow */
            server.playerList.playerJoined(player)
        }
    }

    override suspend fun handleChatMessage(packet: ServerboundChatMessagePacket) {
        server.playerList.broadcastMessage(Component.text("<${connection.player.username}> ${packet.message}"))
    }

    override suspend fun handlePlayerFlyingStatus(packet: FlyingStatusPacket) {
        withContext(server) {
            connection.player.isOnGround = packet.onGround
        }
    }

    override suspend fun handlePlayerPosition(packet: PositionPacket) {
        val currentPos = connection.player.pos

        if (currentPos.distanceSqrt(packet.pos) >= 100) {
            return connection.disconnect(
                String.format(
                    "Invalid movement: (%s, %s, %s) -> (%s, %s, %s)",
                    currentPos.x, currentPos.y, currentPos.z,
                    packet.pos.x, packet.pos.y, packet.pos.z
                )
            )
        }

        /*
         * don't let players move into unloaded areas.
         */
        val chunkPos = ChunkPos(BlockPos(packet.pos))
        val player = connection.player

        if (!player.world.isChunkLoaded(chunkPos)) {
            return connection.sendPacket(
                ClientboundSetAbsolutePlayerPositionPacket(
                    player.pos,
                    player.stance,
                    player.yaw,
                    player.pitch,
                    player.isOnGround,
                )
            )
        }

        /*
        * if all is well, accept the new position.
        */
        withContext(server) {
            player.pos = packet.pos
//             player.stance = packet.stance
        }

        /*
         * also update the player's flying status.
         */
        handlePlayerFlyingStatus(packet)
    }

    override suspend fun handlePlayerRotation(packet: RotationPacket) {
        withContext(server) {
            connection.player.yaw = packet.yaw
            connection.player.pitch = packet.pitch
        }

        /*
        * also update the player's flying status.
        */
        handlePlayerFlyingStatus(packet)
    }

    override suspend fun handleAbsolutePlayerPosition(packet: ServerboundSetAbsolutePlayerPositionPacket) {
        handlePlayerPosition(packet)
        handlePlayerRotation(packet)
    }

    override suspend fun handleDisconnect(packet: GenericDisconnectPacket) {
        connection.close()
    }
}