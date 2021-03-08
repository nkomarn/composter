package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

public class EndTag extends Tag {

    public EndTag() {
        super(null);
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.END;
    }
}
