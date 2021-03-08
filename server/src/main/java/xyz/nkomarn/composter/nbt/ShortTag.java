package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class ShortTag extends Tag {

    private short data;

    public ShortTag(@NotNull String name) {
        super(name);
    }

    public ShortTag(@NotNull String name, short data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.SHORT;
    }
}
