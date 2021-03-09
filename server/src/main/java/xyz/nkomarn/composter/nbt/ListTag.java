package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

public class ListTag extends Tag {

    private List<Tag> data;

    public ListTag(@NotNull String name) {
        super(name);
    }

    public ListTag(@NotNull String name, @NotNull List<Tag> data) {
        super(name);
        this.data = data;
    }

    ListTag(@NotNull DataInputStream data) throws IOException {
        super(new String(data.readNBytes(data.readShort())));
        // TODO parse data
    }

    @Override
    @NotNull
    public Type getType() {
        return Type.LIST;
    }
}
