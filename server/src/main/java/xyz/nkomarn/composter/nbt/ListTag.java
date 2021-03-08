package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListTag extends Tag {

    private List<Tag> data;

    public ListTag(@NotNull String name) {
        super(name);
    }

    public ListTag(@NotNull String name, @NotNull List<Tag> data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.LIST;
    }
}
