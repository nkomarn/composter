package xyz.nkomarn.composter.inventory;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.nbt.CompoundTag;

public class PlayerInventory implements Inventory {

    private final int[] items = new int[36];
    private final int[] armor = new int[4];

    @Override
    public int getSize() {
        return 36;
    }

    @Override
    public int getItem(int slot) {
        return items[slot];
    }

    @Override
    public int setItem(int slot, int item) {
        return items[slot] = item;
    }

    @Override
    public int[] getItems() {
        return items;
    }

    @Override
    public void update() {

    }

    @Override
    public @NotNull CompoundTag toNBT() {
        return null;
    }
}
