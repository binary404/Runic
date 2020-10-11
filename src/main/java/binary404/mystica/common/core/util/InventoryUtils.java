package binary404.mystica.common.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class InventoryUtils {

    public static boolean isPlayerCarryingAmount(PlayerEntity player, final ItemStack stack, final boolean ore) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        int count = stack.getCount();
        for (int var2 = 0; var2 < player.inventory.mainInventory.size(); var2++) {
            if (areItemStacksEqual((ItemStack) player.inventory.mainInventory.get(var2), stack)) {
                count -= ((ItemStack) player.inventory.mainInventory.get(var2)).getCount();
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean areItemStacksEqual(ItemStack stack0, ItemStack stack1) {
        if (stack0 == null && stack1 != null) {
            return false;
        }
        if (stack0 != null && stack1 == null) {
            return false;
        }
        if (stack0 == null && stack1 == null) {
            return true;
        }
        if (stack0.isEmpty() && !stack1.isEmpty()) {
            return false;
        }
        if (!stack0.isEmpty() && stack1.isEmpty()) {
            return false;
        }
        if (stack0.isEmpty() && stack1.isEmpty()) {
            return true;
        }
        boolean t1 = true;
        return stack0.getItem() == stack1.getItem() && t1;
    }

    public static boolean consumePlayerItem(PlayerEntity player, ItemStack item) {
        int count = item.getCount();
        for (int var2 = 0; var2 < player.inventory.mainInventory.size(); var2++) {
            if (areItemStacksEqual(player.inventory.mainInventory.get(var2), item)) {
                if ((player.inventory.mainInventory.get(var2)).getCount() > count) {
                    (player.inventory.mainInventory.get(var2)).shrink(count);
                    count = 0;
                } else {
                    count -= (player.inventory.mainInventory.get(var2)).getCount();
                    player.inventory.mainInventory.set(var2, ItemStack.EMPTY);
                }
                if (count <= 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean areItemStacksEqualForCrafting(ItemStack stack0, Object in) {
        if (stack0 == null && in != null) {
            return false;
        }
        if (stack0 != null && in == null) {
            return false;
        }
        if (stack0 == null && in == null) {
            return true;
        }
        if (in instanceof Object[]) {
            return true;
        }
        if (in instanceof ItemStack) {
            boolean t1;
            boolean bl = t1 = !stack0.hasTag() || InventoryUtils.areItemStacksEqualForCrafting(stack0, (ItemStack) in);
            if (!t1)
                return false;
            return stack0.getItem() == ((ItemStack) in).getItem();
        }
        return false;
    }

    public static boolean areItemStackTagsEqualForCrafting(ItemStack slotItem, ItemStack recipeItem) {
        if (recipeItem == null || slotItem == null) {
            return false;
        }
        if (recipeItem.getTag() != null && slotItem.getTag() == null) {
            return false;
        }
        if (recipeItem.getTag() == null) {
            return true;
        }
        for (String s : recipeItem.getTag().keySet()) {
            if (slotItem.getTag().contains(s)) {
                if (slotItem.getTag().get(s).toString().equals(recipeItem.getTag().get(s).toString()))
                    continue;
                return false;
            }
            return false;
        }
        return true;
    }

    public static void dropItemsAtEntity(World world, BlockPos pos, Entity entity) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (!(tileEntity instanceof IInventory) || world.isRemote) {
            return;
        }
        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);

            if (!item.isEmpty() && item.getCount() > 0) {


                ItemEntity entityItem = new ItemEntity(world, entity.getPosX(), entity.getPosY() + (entity.getEyeHeight() / 2.0F), entity.getPosZ(), item.copy());

                world.addEntity((Entity) entityItem);
                inventory.setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
    }


}
