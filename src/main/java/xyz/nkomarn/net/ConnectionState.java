package xyz.nkomarn.net;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import xyz.nkomarn.protocol.Direction;
import xyz.nkomarn.protocol.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;

public enum ConnectionState {

    HANDSHAKING(new EnumMap<>(Direction.class)),
    STATUS(new EnumMap<>(Direction.class)),
    LOGIN(new EnumMap<>(Direction.class)),
    PLAY(new EnumMap<>(Direction.class));

    private final EnumMap<Direction, Int2ObjectOpenHashMap<Class<? extends Packet<?>>>> map;

    ConnectionState(EnumMap<Direction, Int2ObjectOpenHashMap<Class<? extends Packet<?>>>> map) {
        this.map = map;
    }

    public EnumMap<Direction, Int2ObjectOpenHashMap<Class<? extends Packet<?>>>> getPacketMap() {
        return map;
    }

    public Packet<?> getPacketById(Direction direction, int id) {
        //if (getPacketMap().get(direction) == null) return null;
       // System.out.println(getPacketMap().get(direction).size());
        if (!getPacketMap().computeIfAbsent(direction, direction1 -> new Int2ObjectOpenHashMap<>()).containsKey(id)) {
            return null;
            //throw new IllegalStateException("A packet for ID " + id + " does not exist.");
        }

        try {
            Class<? extends Packet<?>> packetClass = getPacketMap().get(direction).get(id);
            return packetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ConnectionState getById(int id) {
        switch (id) {
            case 1 -> {
                return STATUS;
            }
            case 2 -> {
                return LOGIN;
            }
            case 3 -> {
                return PLAY;
            }
            default -> {
                return HANDSHAKING;
            }
        }
    }
}