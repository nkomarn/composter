package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class ByteTag extends Tag {

    private byte data;

    public ByteTag(@NotNull String name) {
        super(name);
    }

    public ByteTag(@NotNull String name, byte data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.BYTE;
    }
}