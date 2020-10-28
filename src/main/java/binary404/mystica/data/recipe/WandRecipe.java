package binary404.mystica.data.recipe;

import binary404.mystica.api.item.IWand;
import binary404.mystica.api.item.WandCap;
import binary404.mystica.api.item.WandRod;
import binary404.mystica.api.recipe.IArcaneCraftingRecipe;
import binary404.mystica.common.items.ModItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class WandRecipe implements IArcaneCraftingRecipe {

    public static final SpecialRecipeSerializer<WandRecipe> SERIALIZER = new SpecialRecipeSerializer<>(WandRecipe::new);

    private final ResourceLocation id;

    public WandRecipe(ResourceLocation idIn) {
        this.id = idIn;
    }

    @Override
    public int getMystic(CraftingInventory inv) {
        ItemStack cap1 = inv.getStackInSlot(2);
        ItemStack cap2 = inv.getStackInSlot(6);
        ItemStack rod = inv.getStackInSlot(4);

        int cc = -1;
        int cr = -1;

        for (WandCap wc : WandCap.caps.values()) {
            if (cap1.getItem() == wc.getItem().getItem() && cap2.getItem() == wc.getItem().getItem()) {
                cc = wc.getCraftCost();
                break;
            }
        }

        for (WandRod wr : WandRod.rods.values()) {
            if (rod.getItem() == wr.getItem().getItem()) {
                cr = wr.getCraftCost();
                break;
            }
        }

        int cost = cc + cr * 6;

        return cost;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }

    @Override
    public boolean matches(CraftingInventory inv, World worldIn) {

        boolean foundTopCap = false;
        boolean foundBottomCap = false;
        boolean foundRod = false;

        for (WandCap cap : WandCap.caps.values()) {
            ItemStack stack = inv.getStackInSlot(2);
            Item capItem = cap.getItem().getItem();
            if (!stack.isEmpty())
                if (stack.getItem() == capItem)
                    foundTopCap = true;
            stack = inv.getStackInSlot(6);
            if (!stack.isEmpty())
                if (stack.getItem() == capItem)
                    foundBottomCap = true;
        }
        for (String rodId : WandRod.rods.keySet()) {
            WandRod rod = WandRod.rods.get(rodId);
            Item rodItem = rod.getItem().getItem();
            ItemStack stack = inv.getStackInSlot(4);
            if (!stack.isEmpty())
                if (stack.getItem() == rodItem)
                    foundRod = true;
        }
        return foundRod && foundTopCap && foundBottomCap;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(ModItems.wand);
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public ItemStack getCraftingResult(CraftingInventory inv) {
        WandCap wandCap = null;
        WandRod wandRod = null;
        for (String capId : WandCap.caps.keySet()) {
            WandCap cap = WandCap.caps.get(capId);
            Item capItem = cap.getItem().getItem();
            ItemStack stack = inv.getStackInSlot(2);
            if (!stack.isEmpty())
                if (stack.getItem() == capItem)
                    wandCap = cap;

        }

        for (String rodId : WandRod.rods.keySet()) {
            WandRod rod = WandRod.rods.get(rodId);
            Item rodItem = rod.getItem().getItem();
            ItemStack stack = inv.getStackInSlot(4);
            if (!stack.isEmpty())
                if (stack.getItem() == rodItem)
                    wandRod = rod;
        }

        ItemStack wand = new ItemStack(ModItems.wand);

        IWand iWand = (IWand) wand.getItem();

        iWand.setRod(wand, wandRod);
        iWand.setCap(wand, wandCap);

        return wand;
    }

    @Override
    public boolean canFit(int width, int height) {
        return width > 2 && height > 2;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }


}
