package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class DoubleTag extends Tag {

    private double data;

    public DoubleTag(@NotNull String name) {
        super(name);
    }

    public DoubleTag(@NotNull String name, double data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.DOUBLE;
    }
}
