package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class ShortTag extends Tag {

    private short data;

    public ShortTag(@NotNull String name) {
        super(name);
    }

    public ShortTag(@NotNull String name, short data) {
        super(name);
        this.data = data;
    }

    ShortTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = data.readShort();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.SHORT;
    }
}
