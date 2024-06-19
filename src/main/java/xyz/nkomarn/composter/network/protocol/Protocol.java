package xyz.nkomarn.composter.network.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import xyz.nkomarn.composter.network.protocol.packet.play.ClientboundChunkDataPacket;
import xyz.nkomarn.composter.network.protocol.packet.play.ClientboundChunkOperationPacket;
import xyz.nkomarn.composter.network.protocol.packet.play.ServerboundDigPacket;
import xyz.nkomarn.composter.network.protocol.packet.s2c.ClientboundChatPacket;
import xyz.nkomarn.composter.network.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.composter.network.protocol.packet.c2s.*;
import xyz.nkomarn.composter.network.protocol.packet.s2c.*;

import java.lang.reflect.InvocationTargetException;

public class Protocol {

    static {
        /*
         * The keep-alive packet is bi-directional.
         */
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, KeepAliveBiPacket.class); // 0x00
        register(ConnectionState.PLAY, Direction.SERVERBOUND, KeepAliveBiPacket.class); // 0x00
        /*
         * The login packet is bi-directional.
         */
        register(ConnectionState.LOGIN, Direction.CLIENTBOUND, LoginS2CPacket.class); // 0x01
        register(ConnectionState.LOGIN, Direction.SERVERBOUND, LoginC2SPacket.class); // 0x01

        /*
         * The handshake packet is bi-directional.
         */
        register(ConnectionState.HANDSHAKING, Direction.CLIENTBOUND, HandshakeC2SPacket.class); // 0x02
        register(ConnectionState.HANDSHAKING, Direction.SERVERBOUND, HandshakeC2SPacket.class); // 0x02

        /*
         * There are two chat packets, one for each side.
         */
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundChatPacket.class); // 0x03
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, ClientboundChatPacket.class); // 0x03

        register(ConnectionState.PLAY, Direction.CLIENTBOUND, ClientboundSetTimePacket.class); // 0x04
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, SpawnPositionS2CPacket.class); // 0x06


        // register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerC2SPacket.class); // 0x0A
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundOnGroundPacket.class); // 0x0A
        register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerPosC2SPacket.class); // 0x0B
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundPlayerLookPacket.class); // 0x0C
        register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerPosLookC2SPacket.class); // 0x0D

        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundDigPacket.class); // 0x0D

        // register(Direction.CLIENTBOUND, NamedEntitySpawnS2CPacket.class); // 0x14
        // register(Direction.CLIENTBOUND, EntityS2CPacket.class); // 0x1E
        // register(Direction.CLIENTBOUND, EntityTeleportS2CPacket.class); // 0x22

        register(ConnectionState.PLAY, Direction.CLIENTBOUND, ClientboundChunkOperationPacket.class); // 0x32
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, ClientboundChunkDataPacket.class); // 0x33

        // register(Direction.CLIENTBOUND, EffectS2CPacket.class); // 0x33

        register(ConnectionState.PLAY, Direction.CLIENTBOUND, WindowItemsS2CPacket.class); // 0x68
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundPlayerActionPacket.class); // 0x13

        register(ConnectionState.HANDSHAKING, Direction.SERVERBOUND, ServerListPingC2SPacket.class); // 0xFE
        register(ConnectionState.HANDSHAKING, Direction.CLIENTBOUND, DisconnectS2CPacket.class); // 0xFF
        register(ConnectionState.LOGIN, Direction.CLIENTBOUND, DisconnectS2CPacket.class); // 0xFF
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, DisconnectS2CPacket.class); //

        /*
         * Client disconnect notification.
         * TODO: support all connection states.
         */
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundDisconnectPacket.class); // 0xFF
    }

    private static void register(ConnectionState state, Direction direction, Class<? extends Packet<?>> clazz) {
        try {
            state.getPacketMap().computeIfAbsent(direction, a -> new Int2ObjectOpenHashMap<>()).put(clazz.getDeclaredConstructor().newInstance().getId(), clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
