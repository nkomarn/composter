package xyz.nkomarn.composter.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.composter.item.ItemStack;
import xyz.nkomarn.composter.world.World;

public class ItemEntity extends Entity {

    public ItemEntity(@NotNull World world, @NotNull ItemStack item) {
        super(world);
    }
}
