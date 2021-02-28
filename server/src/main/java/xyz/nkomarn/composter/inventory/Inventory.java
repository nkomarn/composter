package xyz.nkomarn.composter.inventory;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.nbt.CompoundTag;

public interface Inventory {

    int getSize();

    int getItem(int slot);

    int setItem(int slot, int item);

    int[] getItems();

    void update();

    @NotNull CompoundTag toNBT();
}
