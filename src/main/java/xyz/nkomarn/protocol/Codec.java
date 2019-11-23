package xyz.nkomarn.protocol;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

public abstract class Codec<T extends Packet> {
    private final Class<T> clazz;
    private final int id;

    public Codec(final Class<T> clazz, final int id) {
        this.clazz = clazz;
        this.id = id;
    }

    public final Class<T> getType() {
        return this.clazz;
    }

    public final int getId() {
        return this.id;
    }

    public abstract ByteBuf encode(T packet) throws IOException;
    public abstract T decode(ByteBuf buffer) throws IOException;
}
