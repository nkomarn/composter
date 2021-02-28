package xyz.nkomarn.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import xyz.nkomarn.net.ConnectionState;
import xyz.nkomarn.protocol.packet.c2s.*;
import xyz.nkomarn.protocol.packet.s2c.StatusPongS2CPacket;
import xyz.nkomarn.protocol.packet.s2c.StatusResponseS2CPacket;

import java.lang.reflect.InvocationTargetException;

public class Protocol {

    private static void register(ConnectionState state, Direction direction, Class<? extends Packet<?>> clazz) {
        try {
            state.getPacketMap().computeIfAbsent(direction, a -> new Int2ObjectOpenHashMap<>()).put(clazz.getDeclaredConstructor().newInstance().getId(), clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    static {
        register(ConnectionState.HANDSHAKING, Direction.C2S, HandshakeC2SPacket.class);
        register(ConnectionState.STATUS, Direction.C2S, StatusPingC2SPacket.class);
        register(ConnectionState.STATUS, Direction.S2C, StatusPongS2CPacket.class);
        register(ConnectionState.STATUS, Direction.C2S, StatusRequestC2SPacket.class);
        register(ConnectionState.STATUS, Direction.S2C, StatusResponseS2CPacket.class);
    }
}
