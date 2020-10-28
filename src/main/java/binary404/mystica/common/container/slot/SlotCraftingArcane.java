package binary404.mystica.common.container.slot;

import binary404.mystica.api.item.IWand;
import binary404.mystica.common.core.recipe.ModRecipeTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHolder;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;

public class SlotCraftingArcane extends Slot {

    private final CraftingInventory craftMatrix;
    private PlayerEntity player;
    private int amountCrafted;

    public SlotCraftingArcane(PlayerEntity player, CraftingInventory inventory, IInventory inventoryIn, int par4, int par5, int par6) {
        super(inventoryIn, par4, par5, par6);
        this.player = player;
        this.craftMatrix = inventory;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (getHasStack()) {
            this.amountCrafted += Math.min(amount, getStack().getCount());
        }
        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.amountCrafted += amount;
        onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        if (this.amountCrafted > 0) {
            stack.onCrafting(this.player.world, this.player, this.amountCrafted);
            BasicEventHooks.firePlayerCraftingEvent(player, stack, this.craftMatrix);
        }
        if (this.inventory instanceof IRecipeHolder) {
            ((IRecipeHolder) this.inventory).onCrafting(player);
        }
        this.amountCrafted = 0;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        onCrafting(stack);
        ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonNullList = thePlayer.world.getRecipeManager().getRecipeNonNull(ModRecipeTypes.ARCANE_CRAFTING_TYPE, this.craftMatrix, thePlayer.world);
        thePlayer.world.getRecipeManager().getRecipe(ModRecipeTypes.ARCANE_CRAFTING_TYPE, this.craftMatrix, thePlayer.world).ifPresent(recipe -> {
            if (recipe.getMystic(this.craftMatrix) > 0 && !this.craftMatrix.getStackInSlot(10).isEmpty()) {
                IWand wand = (IWand) this.craftMatrix.getStackInSlot(10).getItem();
                wand.consumeMystic(this.craftMatrix.getStackInSlot(10), thePlayer, recipe.getMystic(this.craftMatrix), true);
            }
        });
        ForgeHooks.setCraftingPlayer(null);

        for (int i = 0; i < nonNullList.size(); i++) {
            ItemStack stack1 = this.craftMatrix.getStackInSlot(i);
            ItemStack stack2 = nonNullList.get(i);

            if (i == 10)
                continue;

            if (!stack1.isEmpty()) {
                this.craftMatrix.decrStackSize(i, 1);
                stack1 = this.craftMatrix.getStackInSlot(i);
            }

            if (!stack2.isEmpty()) {

                if (stack1.isEmpty()) {
                    this.craftMatrix.setInventorySlotContents(i, stack2);
                }

            } else if (!this.player.inventory.addItemStackToInventory(stack2)) {
                this.player.dropItem(stack2, false);
            }

        }
        return stack;
    }
}
