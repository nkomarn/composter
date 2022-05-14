package xyz.nkomarn.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import xyz.nkomarn.net.ConnectionState;
import xyz.nkomarn.net.Direction;
import xyz.nkomarn.protocol.packet.bi.BidirectionalChatPacket;
import xyz.nkomarn.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.protocol.packet.c2s.*;
import xyz.nkomarn.protocol.packet.s2c.*;

import java.lang.reflect.InvocationTargetException;

public class Protocol {

    /*
    @Nullable
    public Packet<?> getPacketById(int id, @NotNull Direction direction) {
        if (!direction.getPacketMap().containsKey(id)) {
            return null;
            // throw new IllegalStateException("A packet for ID " + id + " does not exist."); TODO bring this back after all packets are implemented
        }

        try {
            Class<? extends Packet<?>> packetClass = direction.getPacketMap().get(id);
            return packetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
     */

    private static void register(ConnectionState state, Direction direction, Class<? extends Packet<?>> clazz) {
        try {
            state.getPacketMap().computeIfAbsent(direction, a -> new Int2ObjectOpenHashMap<>()).put(clazz.getDeclaredConstructor().newInstance().getId(), clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

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
         * The chat packet is bi-directional.
         */
        register(ConnectionState.PLAY, Direction.SERVERBOUND, BidirectionalChatPacket.class); // 0x03
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, BidirectionalChatPacket.class); // 0x03

        register(ConnectionState.PLAY, Direction.CLIENTBOUND, ClientboundSetTimePacket.class); // 0x04
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, SpawnPositionS2CPacket.class); // 0x06


        // register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerC2SPacket.class); // 0x0A
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundOnGroundPacket.class); // 0x0A
        register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerPosC2SPacket.class); // 0x0B
        register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerLookC2SPacket.class); // 0x0C
        register(ConnectionState.PLAY, Direction.SERVERBOUND, PlayerPosLookC2SPacket.class); // 0x0D

        // register(Direction.CLIENTBOUND, NamedEntitySpawnS2CPacket.class); // 0x14
        // register(Direction.CLIENTBOUND, EntityS2CPacket.class); // 0x1E
        // register(Direction.CLIENTBOUND, EntityTeleportS2CPacket.class); // 0x22

        register(ConnectionState.PLAY, Direction.CLIENTBOUND, PreChunkS2CPacket.class); // 0x32
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, MapChunkS2CPacket.class); // 0x33

        // register(Direction.CLIENTBOUND, EffectS2CPacket.class); // 0x33

        register(ConnectionState.PLAY, Direction.CLIENTBOUND, WindowItemsS2CPacket.class); // 0x68
        register(ConnectionState.PLAY, Direction.SERVERBOUND, ServerboundPlayerActionPacket.class); // 0x13

        register(ConnectionState.HANDSHAKING, Direction.SERVERBOUND, ServerListPingC2SPacket.class); // 0xFE
        register(ConnectionState.HANDSHAKING, Direction.CLIENTBOUND, DisconnectS2CPacket.class); // 0xFF
        register(ConnectionState.LOGIN, Direction.CLIENTBOUND, DisconnectS2CPacket.class); // 0xFF
        register(ConnectionState.PLAY, Direction.CLIENTBOUND, DisconnectS2CPacket.class); // 0xFF
    }
}
