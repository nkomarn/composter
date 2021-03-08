package xyz.nkomarn.composter.nbt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A representation of a NBT tag, the base for the
 * NBT data format used in Minecraft.
 */
public abstract class Tag {

    private final String name;

    Tag(@Nullable String name) {
        this.name = name;
    }

    @NotNull
    public abstract Type getType();

    @Nullable
    public String getName() {
        return name;
    }

    /**
     * Represents all possible NBT tag types.
     */
    public enum Type {

        END,
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        BYTE_ARRAY,
        STRING,
        LIST,
        COMPOUND,
        INT_ARRAY,
        LONG_ARRAY;

        public byte getId() {
            return (byte) this.ordinal();
        }

        @NotNull
        public static Type fromId(byte id) {
            return values()[id];
        }
    }
}
