package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class IntArrayTag extends Tag {

    private int[] data;

    public IntArrayTag(@NotNull String name) {
        super(name);
    }

    public IntArrayTag(@NotNull String name, int[] data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.INT_ARRAY;
    }
}
