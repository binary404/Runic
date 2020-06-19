package binary404.runic.common.tile;

import com.google.common.collect.Lists;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class TileInventory extends TileMod {

    public TileInventory(TileEntityType type) {
        super(type);
    }

}
