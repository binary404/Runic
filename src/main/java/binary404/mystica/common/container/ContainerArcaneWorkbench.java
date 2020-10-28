package binary404.mystica.common.container;

import binary404.mystica.api.item.IWand;
import binary404.mystica.common.container.slot.SlotCraftingArcane;
import binary404.mystica.common.core.recipe.ModRecipeTypes;
import binary404.mystica.common.tile.crafting.TileArcaneWorkbench;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;

import java.util.Optional;

public class ContainerArcaneWorkbench extends CommonContainer {

    public TileArcaneWorkbench tile;
    private PlayerInventory playerInventory;

    public ContainerArcaneWorkbench(PlayerInventory playerInventory, TileArcaneWorkbench e, int id) {
        super(ModContainers.arcane_workbench, id, 10);
        this.tile = e;
        this.playerInventory = playerInventory;
        this.tile.inventory.eventHandler = this;

        addSlot(new SlotCraftingArcane(playerInventory.player, this.tile.inventory, this.tile.inventory, 9, 160, 64));

        addSlot(new Slot(this.tile.inventory, 10, 95, 35));

        for (int var6 = 0; var6 < 3; var6++) {
            for (int var7 = 0; var7 < 3; var7++) {
                addSlot(new Slot(this.tile.inventory, var7 + var6 * 3, 30 + var7 * 18, 17 + var6 * 18));
            }
        }

        for (int var6 = 0; var6 < 3; var6++) {
            for (int var7 = 0; var7 < 9; var7++) {
                addSlot(new Slot(playerInventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
            }
        }

        for (int var6 = 0; var6 < 9; var6++) {
            addSlot(new Slot(playerInventory, var6, 8 + var6 * 18, 142));
        }
        onCraftMatrixChanged(this.tile.inventory);
    }

    public static ContainerArcaneWorkbench create(int id, PlayerInventory inventory, PacketBuffer buffer) {
        return new ContainerArcaneWorkbench(inventory, (TileArcaneWorkbench) inventory.player.world.getTileEntity(buffer.readBlockPos()), id);
    }


    @Override
    public void onCraftMatrixChanged(IInventory inventoryIn) {
        CraftingInventory ic = new CraftingInventory(new ContainerDummy(), 3, 3);
        for (int a = 0; a < 9; a++) {
            ic.setInventorySlotContents(a, this.tile.inventory.getStackInSlot(a));
        }
        if (!this.tile.getWorld().isRemote) {
            Optional<ICraftingRecipe> recipeOptional = this.tile.getWorld().getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, ic, this.tile.getWorld());

            ICraftingRecipe craftingRecipe = recipeOptional.orElse(null);

            if (craftingRecipe != null) {
                this.tile.inventory.setInventorySlotContentsSoftly(9, craftingRecipe.getCraftingResult(ic));
                ServerPlayerEntity player = (ServerPlayerEntity) playerInventory.player;
                player.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, craftingRecipe.getCraftingResult(ic)));
            } else {
                this.tile.inventory.setInventorySlotContentsSoftly(9, ItemStack.EMPTY);
                ServerPlayerEntity player = (ServerPlayerEntity) playerInventory.player;
                player.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, ItemStack.EMPTY));
            }

            if (this.tile.inventory.getStackInSlot(9).isEmpty() && !this.tile.inventory.getStackInSlot(10).isEmpty() && this.tile.inventory.getStackInSlot(10).getItem() instanceof IWand) {
                IWand wand = (IWand) this.tile.inventory.getStackInSlot(10).getItem();
                this.tile.getWorld().getServer().getRecipeManager().getRecipe(ModRecipeTypes.ARCANE_CRAFTING_TYPE, ic, this.tile.getWorld()).ifPresent(recipe -> {
                    if (wand.consumeMystic(this.tile.inventory.getStackInSlot(10), this.playerInventory.player, recipe.getMystic(ic), false)) {
                        this.tile.inventory.setInventorySlotContentsSoftly(9, recipe.getCraftingResult(ic));
                        ServerPlayerEntity player = (ServerPlayerEntity) playerInventory.player;
                        player.connection.sendPacket(new SSetSlotPacket(this.windowId, 0, recipe.getCraftingResult(ic)));
                    }
                });
            }
        }
        this.tile.markDirty();
        detectAndSendChanges();
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        super.onContainerClosed(playerIn);

        if (!(this.tile.getWorld()).isRemote)
            this.tile.inventory.eventHandler = new ContainerDummy();
    }


    public ItemStack transferStackInSlot(PlayerEntity par1EntityPlayer, int par1) {
        ItemStack var2 = ItemStack.EMPTY;
        Slot var3 = this.inventorySlots.get(par1);

        if (var3 != null && var3.getHasStack()) {

            ItemStack var4 = var3.getStack();
            var2 = var4.copy();

            if (par1 == 0) {

                if (!mergeItemStack(var4, 11, 47, true)) {
                    return ItemStack.EMPTY;
                }

                var3.onSlotChange(var4, var2);
            } else if (!mergeItemStack(var4, 11, 38, false)) {

                return ItemStack.EMPTY;
            }

            if (var4.getCount() == 0) {

                var3.putStack(ItemStack.EMPTY);
            } else {

                var3.onSlotChanged();
            }

            if (var4.getCount() == var2.getCount()) {
                return ItemStack.EMPTY;
            }

            var3.onTake(this.playerInventory.player, var4);
        }

        return var2;
    }
}
