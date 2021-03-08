package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class StringTag extends Tag {

    private String data;

    public StringTag(@NotNull String name) {
        super(name);
    }

    public StringTag(@NotNull String name, @NotNull String data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.STRING;
    }
}
