package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;

public class StringTag extends Tag {

    private String data;

    public StringTag(@NotNull String name) {
        super(name);
    }

    public StringTag(@NotNull DataInputStream in) throws IOException {
        super("");
        var nameLength = in.readNBytes(2)[1];
        this.name = new String(in.readNBytes(nameLength));
        var dataLength = in.readNBytes(2)[1];
        this.data = new String(in.readNBytes(dataLength));

        System.out.println(data);
    }

    public StringTag(@NotNull String name, @NotNull String data) {
        super(name);
        this.data = data;
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.STRING;
    }
}
