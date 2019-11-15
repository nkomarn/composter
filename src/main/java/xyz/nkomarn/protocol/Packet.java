package xyz.nkomarn.protocol;

import io.netty.buffer.ByteBuf;

public abstract class Packet<T> {
    public abstract ByteBuf encode(T message);
    public abstract T decode(ByteBuf buffer);
}
