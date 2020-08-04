package xyz.nkomarn.entity;

import org.jetbrains.annotations.NotNull;
import xyz.nkomarn.item.ItemStack;
import xyz.nkomarn.type.Location;
import xyz.nkomarn.world.World;

public class ItemEntity extends Entity {

    public ItemEntity(@NotNull World world, @NotNull Location location, @NotNull ItemStack item) {
        super(world);
    }
}
