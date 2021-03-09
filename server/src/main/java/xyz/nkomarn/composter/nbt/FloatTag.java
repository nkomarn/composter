package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class FloatTag extends Tag {

    private float data;

    public FloatTag(@NotNull String name) {
        super(name);
    }

    public FloatTag(@NotNull String name, float data) {
        super(name);
        this.data = data;
    }

    FloatTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        this.data = data.readFloat();
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.FLOAT;
    }
}
