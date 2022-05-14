package xyz.nkomarn.composter.network.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.jetbrains.annotations.NotNull;

public abstract class Packet<T> {

    public int getId() {
        return 0x0;
    }

    public @NotNull ByteBuf encode() {
        return Unpooled.EMPTY_BUFFER;
    }

    public T decode(@NotNull ByteBuf buffer) {
        return null;
    }
}
