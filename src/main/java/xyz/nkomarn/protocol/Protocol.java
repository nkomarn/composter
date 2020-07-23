package xyz.nkomarn.protocol;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.protocol.packet.bi.KeepAliveBiPacket;
import xyz.nkomarn.protocol.packet.c2s.*;
import xyz.nkomarn.protocol.packet.s2c.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Protocol {

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

    private static void register(@NotNull Direction direction, @NotNull Class<? extends Packet<?>> clazz) {
        try {
            direction.getPacketMap().put(clazz.getDeclaredConstructor().newInstance().getId(), clazz);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /*public static <T extends Packet> Codec<T> getCodec(final Class<T> clazz) {
        return (Codec<T>) packetMappings.get(clazz); // TODO check cast
    }*/

    static {
        register(Direction.BI, KeepAliveBiPacket.class);
        register(Direction.C2S, LoginC2SPacket.class);
        register(Direction.S2C, LoginS2CPacket.class);
        register(Direction.C2S, HandshakeC2SPacket.class);
        register(Direction.S2C, HandshakeC2SPacket.class);

        register(Direction.S2C, SpawnPositionS2CPacket.class); // 0x06

        register(Direction.C2S, PlayerPosC2SPacket.class); // 0x0B
        register(Direction.C2S, PlayerLookC2SPacket.class); // 0x0C
        register(Direction.C2S, PlayerPosLookC2SPacket.class); // 0x0D
        register(Direction.S2C, PlayerPosLookS2CPacket.class); // 0x0D

        register(Direction.S2C, PreChunkS2CPacket.class); // 0x32
        register(Direction.S2C, MapChunkS2CPacket.class); // 0x33

        register(Direction.S2C, DisconnectS2CPacket.class);
        /*register(HandshakeCodec.class);
        register(ChatCodec.class);
        register(AnimationCodec.class);
        register(SpawnPositionPacket.class);
        register(DisconnectCodec.class);
        register(PlayerPositionCodec.class);
        register(PlayerPositionAndLookCodec.class);
        register(PreChunkCodec.class);
        register(MapChunkCodec.class);*/
    }

    public enum Direction {

        C2S(new HashMap<>()),
        S2C(new HashMap<>()),
        BI(new HashMap<>());

        private final Map<Integer, Class<? extends Packet<?>>> map;

        Direction(@NotNull HashMap<Integer, Class<? extends Packet<?>>> map) {
            this.map = map;
        }

        Map<Integer, Class<? extends Packet<?>>> getPacketMap() {
            return map;
        }
    }
}
