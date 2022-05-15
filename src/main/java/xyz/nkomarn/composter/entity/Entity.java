package xyz.nkomarn.composter.entity;

public abstract class Entity {

    private boolean removed;

    public boolean isRemoved() {
        return removed;
    }

    public void markRemoved() {
        this.removed = true;
    }
}
