package xyz.nkomarn.protocol;

import xyz.nkomarn.protocol.codec.*;

import java.util.HashMap;
import java.util.Map;

public class CodecHandler {

    // Array of all opcodes and their corresponding class
    // The index represents the opcode
    private static Codec<?>[] codecs = new Codec<?>[256];
    private static Map<Class<? extends Packet>, Codec<?>> classes =
        new HashMap<>();

    static {
        try {
            register(KeepAliveCodec.class);
            register(LoginCodec.class);
            register(HandshakeCodec.class);
            register(ChatCodec.class);
            register(SpawnPositionPacket.class);
            register(DisconnectCodec.class);
            register(PlayerPositionAndLookCodec.class);
            register(PreChunkCodec.class);
            register(MapChunkCodec.class);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace(); // TODO handle somehow
        }
    }

    private static <T extends Packet, C extends Codec<T>> void register(final Class<C> clazz)
        throws InstantiationException, IllegalAccessException {
        Codec<T> codec = clazz.newInstance();
        codecs[codec.getId()] = codec;
        classes.put(codec.getType(), codec);
    }

    public static Codec<?> getCodec(final int id) {
        return codecs[id];
    }

    public static <T extends Packet> Codec<T> getCodec(final Class<T> clazz) {
        return (Codec<T>) classes.get(clazz); // TODO check cast
    }
}
