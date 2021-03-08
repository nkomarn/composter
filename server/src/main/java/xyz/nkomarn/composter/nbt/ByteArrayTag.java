package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class ByteArrayTag extends Tag {

    private byte[] data;

    public ByteArrayTag(@NotNull String name) {
        super(name);
    }

    public ByteArrayTag(@NotNull String name, byte[] data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.BYTE_ARRAY;
    }
}
