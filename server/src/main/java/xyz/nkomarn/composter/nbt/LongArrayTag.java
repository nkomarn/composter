package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class LongArrayTag extends Tag {

    private long[] data;

    public LongArrayTag(@NotNull String name) {
        super(name);
    }

    public LongArrayTag(@NotNull String name, long[] data) {
        super(name);
        this.data = data;
    }

    LongArrayTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        // TODO parse data
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.LONG_ARRAY;
    }
}
