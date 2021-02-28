package xyz.nkomarn.nbt;

public abstract class Tag {

    private final String name;

    public Tag() {
        this("");
    }

    public Tag(final String name) {
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }

    public abstract Object getValue();

}
