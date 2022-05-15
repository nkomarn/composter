package xyz.nkomarn.composter.network.protocol;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.jetbrains.annotations.Nullable;
import xyz.nkomarn.composter.network.FlowDirection;
import xyz.nkomarn.composter.network.protocol.packet.Packet;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumMap;

public enum ConnectionState {

    HANDSHAKING(new EnumMap<>(FlowDirection.class)),
    LOGIN(new EnumMap<>(FlowDirection.class)),
    PLAY(new EnumMap<>(FlowDirection.class));

    private final EnumMap<FlowDirection, Int2ObjectOpenHashMap<Class<? extends Packet>>> map;


    ConnectionState(EnumMap<FlowDirection, Int2ObjectOpenHashMap<Class<? extends Packet>>> map) {
        this.map = map;
    }

    public EnumMap<FlowDirection, Int2ObjectOpenHashMap<Class<? extends Packet>>> packets() {
        return map;
    }

    @Nullable
    public Packet packetById(FlowDirection direction, int id) {
        if (!packets().computeIfAbsent(direction, direction1 -> new Int2ObjectOpenHashMap<>()).containsKey(id)) {
            return null;
        }

        try {
            var packetClass = packets().get(direction).get(id);
            return packetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }
}