package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class LongArrayTag extends Tag {

    private long[] data;

    public LongArrayTag(@NotNull String name) {
        super(name);
    }

    public LongArrayTag(@NotNull String name, long[] data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.LONG_ARRAY;
    }
}
