package xyz.nkomarn.net;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.protocol.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;

public enum ConnectionState {

    HANDSHAKING(new EnumMap<>(Direction.class)),
    LOGIN(new EnumMap<>(Direction.class)),
    PLAY(new EnumMap<>(Direction.class));

    private final EnumMap<Direction, Int2ObjectOpenHashMap<Class<? extends Packet<?>>>> map;

    ConnectionState(EnumMap<Direction, Int2ObjectOpenHashMap<Class<? extends Packet<?>>>> map) {
        this.map = map;
    }

    public EnumMap<Direction, Int2ObjectOpenHashMap<Class<? extends Packet<?>>>> getPacketMap() {
        return map;
    }

    @Nullable
    public Packet<?> getPacketById(Direction direction, int id) {
        if (!getPacketMap().computeIfAbsent(direction, direction1 -> new Int2ObjectOpenHashMap<>()).containsKey(id)) {
            return null;
        }

        try {
            var packetClass = getPacketMap().get(direction).get(id);
            return packetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}