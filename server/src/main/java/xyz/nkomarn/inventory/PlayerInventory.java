package xyz.nkomarn.inventory;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.nbt.ByteTag;
import xyz.nkomarn.nbt.CompoundTag;
import xyz.nkomarn.nbt.ShortTag;
import xyz.nkomarn.nbt.Tag;

import java.util.HashMap;
import java.util.Map;

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
