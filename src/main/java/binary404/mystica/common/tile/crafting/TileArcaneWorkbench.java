package binary404.mystica.common.tile.crafting;

import binary404.mystica.common.container.ContainerDummy;
import binary404.mystica.common.container.inventory.InventoryArcaneWorkbench;
import binary404.mystica.common.tile.ModTiles;
import binary404.mystica.common.tile.TileMod;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class TileArcaneWorkbench extends TileMod {

    public InventoryArcaneWorkbench inventory = new InventoryArcaneWorkbench(new ContainerDummy(), 3, 3);

    public TileArcaneWorkbench() {
        super(ModTiles.ARCANE_WORKBENCH);
    }

    @Override
    public void read(BlockState state, CompoundNBT par1nbtTagCompound) {
        super.read(state, par1nbtTagCompound);
        NonNullList<ItemStack> stacks = NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(par1nbtTagCompound, stacks);
        for (int a = 0; a < stacks.size(); a++) {
            this.inventory.setInventorySlotContents(a, stacks.get(a));
        }
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        super.write(par1nbtTagCompound);

        NonNullList<ItemStack> stacks = NonNullList.withSize(this.inventory.getSizeInventory(), ItemStack.EMPTY);
        for (int a = 0; a < stacks.size(); a++) {
            stacks.set(a, this.inventory.getStackInSlot(a));
        }
        ItemStackHelper.saveAllItems(par1nbtTagCompound, stacks);
        return par1nbtTagCompound;
    }


}
