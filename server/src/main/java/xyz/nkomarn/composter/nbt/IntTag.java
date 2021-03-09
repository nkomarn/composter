package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class IntTag extends Tag {

    private int data;

    public IntTag(@NotNull String name) {
        super(name);
    }

    public IntTag(@NotNull String name, int data) {
        super(name);
        this.data = data;
    }

    IntTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = data.readInt();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.INT;
    }
}
