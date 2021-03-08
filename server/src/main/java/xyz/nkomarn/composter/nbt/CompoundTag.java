package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundTag extends Tag {

    private final Map<String, Tag> data;

    public CompoundTag(@NotNull String name) {
        super(name);
        this.data = new HashMap<>();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.COMPOUND;
    }
}
