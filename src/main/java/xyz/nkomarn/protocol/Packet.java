package xyz.nkomarn.protocol;

import io.netty.buffer.ByteBuf;

public interface Packet<T> {
    ByteBuf encode(T packet);
    T decode(ByteBuf data);
}
