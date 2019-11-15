package xyz.nkomarn.protocol.scaffold;

import io.netty.buffer.ByteBuf;
import xyz.nkomarn.protocol.Packet;

import java.io.IOException;

public abstract class PacketScaffold<T extends Packet> {

    private final Class<T> clazz;
    private final int id;

    public PacketScaffold(final Class<T> clazz, final int id) {
        this.clazz = clazz;
        this.id = id;
    }

    public final Class<T> getType() {
        return this.clazz;
    }

    public final int getId() {
        return this.id;
    }

    public abstract ByteBuf encode(T message) throws IOException;
    public abstract T decode(ByteBuf buffer) throws IOException;

}
