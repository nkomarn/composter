package kyta.composter.protocol.packet

import kyta.composter.item.Item
import kyta.composter.item.ItemStack
import kyta.composter.item.isEmpty
import kyta.composter.item.shrink
import kyta.composter.item.split
import kyta.composter.network.Connection
import kyta.composter.protocol.ConnectionState
import kyta.composter.protocol.PacketHandler
import kyta.composter.protocol.packet.handshaking.ClientboundHandshakePacket
import kyta.composter.protocol.packet.handshaking.ServerboundHandshakePacket
import kyta.composter.protocol.packet.login.ClientboundLoginPacket
import kyta.composter.protocol.packet.login.ServerboundLoginPacket
import kyta.composter.protocol.packet.play.ClientboundMenuTransactionPacket
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
import kyta.composter.server.withContext
import kyta.composter.world.BlockPos
import kyta.composter.world.ChunkPos
import kyta.composter.world.block.Block
import kyta.composter.world.block.BlockState
import kyta.composter.world.block.STONE
import kyta.composter.world.breakBlock
import kyta.composter.world.dimension.DimensionType
import net.kyori.adventure.text.Component
import kyta.composter.world.entity.Player
import kyta.composter.world.entity.drop
import kyta.composter.world.entity.heldItem
import kyta.composter.world.entity.swingArm

class VanillaPacketHandler(
    private val server: MinecraftServer,
    private val connection: Connection,
) : PacketHandler {
    private lateinit var player: Player

    override suspend fun handleHandshake(packet: ServerboundHandshakePacket) {
        connection.sendPacket(ClientboundHandshakePacket("-"))
        connection.state = ConnectionState.LOGIN
    }

    override suspend fun handleKeepAlive(packet: GenericKeepAlivePacket) {
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

            player = Player(
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
            ).sync()
            connection.player = player
            connection.state = ConnectionState.PLAY

            /* finish up the rest of the joining flow */
            server.playerList.playerJoined(player)
        }
    }

    override suspend fun handleChatMessage(packet: ServerboundChatMessagePacket) {
        if (packet.message.isBlank()) return
        connection.player.inventory.insert(ItemStack(Item(STONE.networkId), 64, 0))
        server.playerList.broadcastMessage(Component.text("<${connection.player.username}> ${packet.message}"))
    }

    override suspend fun handlePlayerFlyingStatus(packet: FlyingStatusPacket) {
        withContext(server) {
            connection.player.isOnGround = packet.onGround
        }
    }

    override suspend fun handlePlayerPosition(packet: PositionPacket) {
        val currentPos = connection.player.pos

        if (currentPos.distanceSqRt(packet.pos) >= 100) {
            /*
            return connection.disconnect(
                String.format(
                    "Invalid movement: (%s, %s, %s) -> (%s, %s, %s)",
                    currentPos.x, currentPos.y, currentPos.z,
                    packet.pos.x, packet.pos.y, packet.pos.z
                )
            )
             */
        }

        /*
         * don't let players move into unloaded areas.
         */
        val chunkPos = ChunkPos(BlockPos(packet.pos))
        val player = connection.player

        if (!player.world.chunks.isLoaded(chunkPos)) {
            connection.sendPacket(
                ClientboundSetAbsolutePlayerPositionPacket(
                    player.pos,
                    player.stance,
                    player.yaw,
                    player.pitch,
                    player.isOnGround,
                )
            )

            return
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
        when (packet.action) {
            ServerboundPlayerDigPacket.Action.START -> {}
            ServerboundPlayerDigPacket.Action.FINISH -> {
                /**
                 * todo;
                 * - perform range checks for block breaking (4 block radius?)
                 * - perform speed checks (are we breaking the block too fast, hacking?)
                 */
                player.world.breakBlock(packet.blockPos)
            }

            /**
             * Drop a single count out of the item stack the
             * player is currently holding in their hand.
             */
            ServerboundPlayerDigPacket.Action.DROP_ITEM -> {
                if (player.heldItem.isEmpty) return@withContext

                val (remainder, singleCount) = player.heldItem.split(1)
                player.heldItem = remainder
                player.drop(singleCount)
            }
        }
    }

    override suspend fun handleBlockPlacement(packet: ServerboundPlaceBlockPacket) = withContext(server) {
        if (!packet.isBlockPlacement()) {
            return@withContext
        }

        val item = player.heldItem.takeUnless(ItemStack::isEmpty)
            ?: return@withContext

        val offset = packet.face.offset
        val target = packet.blockPos.add(offset.x, offset.y, offset.z)

        /**
         * Check world collisions to make sure there is not another
         * block or an entity within the bounding box of the target block.
         */
        if (!player.world.getBlock(target).isAir() || player.world.getCollidingEntities(target.boundingBox).any()) {
            return@withContext
        }

        /**
         * todo; some considerations:
         * - make sure the player can actually reach the target block (4 block reach radius?)
         * - get the block from the registry based on id instead of making a new instance
         */
        player.world.setBlock(target, BlockState(Block(item.item.networkId), item.metadataValue))
        player.heldItem = item.shrink(1)
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
        val openMenu = player.menuSynchronizer.currentMenu

        if (packet.windowId != openMenu.id) {
            return@withContext connection.disconnect("Tried to interact with an invalid menu (#${packet.windowId})")
        }

        val state = openMenu.incrementState()
        if (state != packet.stateId) {
            player.world.server.logger.warn("menu state mismatch for ${player.username} ($state, ${packet.stateId})")
            connection.sendPacket(ClientboundMenuTransactionPacket(packet.windowId, packet.stateId, false))
            player.menuSynchronizer.synchronize()
            return@withContext
        }

        try {
            openMenu.interact(player, packet)
            connection.sendPacket(ClientboundMenuTransactionPacket(packet.windowId, packet.stateId, true))
        } catch (x: Throwable) {
            connection.sendPacket(ClientboundMenuTransactionPacket(packet.windowId, packet.stateId, false))
            player.menuSynchronizer.synchronize()
        }
    }

    override suspend fun handleMenuClose(packet: ServerboundCloseMenuPacket) = withContext(server) {
        player.menuSynchronizer.closeMenu(packet.id)
    }

    override suspend fun handleDisconnect(packet: GenericDisconnectPacket) {
        connection.close()
    }
}