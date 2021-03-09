package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class IntArrayTag extends Tag {

    private int[] data;

    public IntArrayTag(@NotNull String name) {
        super(name);
    }

    public IntArrayTag(@NotNull String name, int[] data) {
        super(name);
        this.data = data;
    }

    IntArrayTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        // TODO parse data
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.INT_ARRAY;
    }
}
