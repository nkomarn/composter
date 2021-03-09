package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class DoubleTag extends Tag {

    private double data;

    public DoubleTag(@NotNull String name) {
        super(name);
    }

    public DoubleTag(@NotNull String name, double data) {
        super(name);
        this.data = data;
    }

    DoubleTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = data.readDouble();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.DOUBLE;
    }
}
