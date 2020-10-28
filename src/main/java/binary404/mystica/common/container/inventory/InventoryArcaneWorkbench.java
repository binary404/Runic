package binary404.mystica.common.container.inventory;

import binary404.mystica.api.item.IWand;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;

import javax.annotation.Nullable;

public class InventoryArcaneWorkbench extends CraftingInventory {

    public InventoryArcaneWorkbench(Container eventHandlerIn, int width, int height) {
        super(eventHandlerIn, width, height);
        this.stackList = NonNullList.withSize(width * height + 2, ItemStack.EMPTY);
    }

    public ItemStack decrStackSize(int index, int count) {
        ItemStack itemstack = ItemStackHelper.getAndSplit(this.stackList, index, count);
        this.eventHandler.onCraftMatrixChanged(this);

        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        super.setInventorySlotContents(index, stack);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 10 && !stack.isEmpty()) {
            if (!(stack.getItem() instanceof IWand))
                return false;
            return true;
        }
        return true;
    }

    public void setInventorySlotContentsSoftly(int index, ItemStack stack) {
        this.stackList.set(index, stack);
    }
}
