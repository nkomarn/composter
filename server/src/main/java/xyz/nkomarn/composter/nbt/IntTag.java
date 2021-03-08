package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class IntTag extends Tag {

    private int data;

    public IntTag(@NotNull String name) {
        super(name);
    }

    public IntTag(@NotNull String name, int data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.INT;
    }
}
