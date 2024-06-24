package kyta.composter.protocol.packet

import kyta.composter.entity.ItemEntity
import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.item.isEmpty
import kyta.composter.item.shrink
import kyta.composter.math.Vec3d
import kyta.composter.network.Connection
import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.packet.handshaking.ClientboundHandshakePacket
import kyta.composter.protocol.packet.handshaking.ServerboundHandshakePacket
import kyta.composter.protocol.packet.login.ClientboundLoginPacket
import kyta.composter.protocol.packet.login.ServerboundLoginPacket
import kyta.composter.protocol.packet.play.ClientboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.FlyingStatusPacket
import kyta.composter.protocol.packet.play.GenericPlayerActionPacket
import kyta.composter.protocol.packet.play.PositionPacket
import kyta.composter.protocol.packet.play.RotationPacket
import kyta.composter.protocol.packet.play.ServerboundChatMessagePacket
import kyta.composter.protocol.packet.play.ServerboundCloseMenuPacket
import kyta.composter.protocol.packet.play.ServerboundMenuInteractionPacket
import kyta.composter.protocol.packet.play.ServerboundPlaceBlockPacket
import kyta.composter.protocol.packet.play.ServerboundPlayerDigPacket
import kyta.composter.protocol.packet.play.ServerboundSetAbsolutePlayerPositionPacket
import kyta.composter.protocol.packet.play.ServerboundSetHeldSlotPacket
import kyta.composter.server.MinecraftServer
import kyta.composter.withContext
import kyta.composter.world.BlockPos
import kyta.composter.world.ChunkPos
import kyta.composter.world.block.AIR
import kyta.composter.world.block.Block
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.STONE
import kyta.composter.world.block.defaultState
import kyta.composter.world.dimension.DimensionType
import net.kyori.adventure.text.Component
import xyz.nkomarn.composter.entity.Player

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
                server.worldManager.primaryWorld,
                connection,
                packet.username,
            )

            /* acknowledge login, advance connection status */
            connection.sendPacket(
                ClientboundLoginPacket(
                    player.id,
                    "composter",
                    player.world.properties.seed,
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
        if (packet.message.isBlank()) return
        connection.player.inventory.insert(ItemStack(Item(STONE.id), 64, 0))
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

        if (!player.world.chunks.isLoaded(chunkPos)) {
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

    override suspend fun handlePlayerDig(packet: ServerboundPlayerDigPacket) = withContext(server) {
        if (packet.action == ServerboundPlayerDigPacket.Action.FINISH) {
            val world = connection.player.world
            val block = world.getBlock(packet.blockPos)

            world.setBlock(packet.blockPos, AIR.defaultState)
            ItemEntity(world, ItemStack(Item(block.block.id), 1, block.metadataValue)).let {
                it.pos = Vec3d(packet.blockPos).add(0.5, 0.25, 0.5)
                world.addEntity(it)
            }
        }
    }

    override suspend fun handleBlockPlacement(packet: ServerboundPlaceBlockPacket) {
        println(packet)
        val player = connection.player
        val selectedItem = player.inventory.getItem(player.selectedHotbarSlot)
        if (selectedItem.isEmpty) return

        // connection.player.sendMessage(Component.text("trying to place item id $selectedItem"))

        /**
         * todo; some considerations:
         * - check block collisions to make sure there is not an entity/block in the target block
         * - make sure the player can actually reach the target block (4 block reach radius?)
         * - get the block from the registry based on id instead of making a new instance
         */
        selectedItem.shrink(1)

        val offset = packet.face.offset
        player.world.setBlock(
            packet.blockPos.add(offset.x.toInt(), offset.y.toInt(), offset.z.toInt()),
            BlockState(Block(selectedItem.item.networkId), selectedItem.metadataValue),
        )
    }

    override suspend fun handlePlayerAction(packet: GenericPlayerActionPacket) {
        when (packet.action) {
            GenericPlayerActionPacket.Action.SWING_ARM -> connection.player.swingArm()
            else -> return
        }
    }

    override suspend fun handleHeldSlotChange(packet: ServerboundSetHeldSlotPacket) = withContext(server) {
        connection.player.selectedHotbarSlot = packet.slot
    }

    override suspend fun handleMenuInteraction(packet: ServerboundMenuInteractionPacket) = withContext(server) {
        val currentMenu = connection.player.currentMenu
            ?: return@withContext

        val state = currentMenu.incrementState()
        if (state != packet.stateId) {
            connection.player.world.server.logger.warn("menu state mismatch for ${connection.player.username} (${state}, ${packet.stateId})")
            return@withContext
        }

        currentMenu.interact(connection.player, packet)
    }

    override suspend fun handleMenuClose(packet: ServerboundCloseMenuPacket) = withContext(server) {
        connection.player.currentMenu = null
    }

    override suspend fun handleDisconnect(packet: GenericDisconnectPacket) {
        connection.close()
    }
}