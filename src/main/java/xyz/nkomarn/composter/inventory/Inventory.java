package xyz.nkomarn.composter.inventory;

public interface Inventory {

    int getSize();

    int getItem(int slot);

    int setItem(int slot, int item);

    int[] getItems();

    void update();
}
