package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class LongTag extends Tag {

    private long data;

    public LongTag(@NotNull String name) {
        super(name);
    }

    public LongTag(@NotNull String name, long data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.LONG;
    }
}
