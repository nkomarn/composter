package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class FloatTag extends Tag {

    private float data;

    public FloatTag(@NotNull String name) {
        super(name);
    }

    public FloatTag(@NotNull String name, float data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.FLOAT;
    }
}
