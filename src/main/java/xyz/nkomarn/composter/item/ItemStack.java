package xyz.nkomarn.composter.item;

import org.jetbrains.annotations.NotNull;

public class ItemStack {

    private final Material material;
    private int amount;

    public ItemStack(@NotNull Material material) {
        this.material = material;
    }

    public ItemStack(@NotNull Material material, int amount) {
        this.material = material;
        this.amount = amount;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
