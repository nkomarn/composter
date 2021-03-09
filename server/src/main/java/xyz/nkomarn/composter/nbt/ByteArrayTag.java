package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class ByteArrayTag extends Tag {

    private byte[] data;

    public ByteArrayTag(@NotNull String name) {
        super(name);
    }

    public ByteArrayTag(@NotNull String name, byte[] data) {
        super(name);
        this.data = data;
    }

    ByteArrayTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        // TODO parse data
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.BYTE_ARRAY;
    }
}
