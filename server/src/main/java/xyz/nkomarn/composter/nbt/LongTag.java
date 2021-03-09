package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class LongTag extends Tag {

    private long data;

    public LongTag(@NotNull String name) {
        super(name);
    }

    public LongTag(@NotNull String name, long data) {
        super(name);
        this.data = data;
    }

    LongTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = data.readLong();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.LONG;
    }
}
