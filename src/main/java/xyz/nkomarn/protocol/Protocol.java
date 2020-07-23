package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.codec.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class CodecHandler {

    private static Map<Integer, Class<? extends Packet>> packetMappings = new HashMap<>();

    private static void register(int id, Class<? extends Packet> clazz) {
        packetMappings.put(id, clazz);
    }

    public static Packet getPacketById(int id) {
        if (!packetMappings.containsKey(id)) {
            throw new IllegalStateException("A packet for ID " + id + " does not exist.");
        }

        try {
            Class<? extends Packet> packetClass = packetMappings.get(id);
            return packetClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends Packet> Codec<T> getCodec(final Class<T> clazz) {
        return (Codec<T>) packetMappings.get(clazz); // TODO check cast
    }

    static {
        register(0x00, KeepAliveCodec.class);
        register(LoginCodec.class);
        register(HandshakeCodec.class);
        register(ChatCodec.class);
        register(AnimationCodec.class);
        register(SpawnPositionPacket.class);
        register(DisconnectCodec.class);
        register(PlayerPositionCodec.class);
        register(PlayerPositionAndLookCodec.class);
        register(PreChunkCodec.class);
        register(MapChunkCodec.class);
    }
}
