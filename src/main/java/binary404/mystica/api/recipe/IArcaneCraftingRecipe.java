package binary404.mystica.api.recipe;

import binary404.mystica.Mystica;
import binary404.mystica.api.item.WandCap;
import binary404.mystica.common.core.recipe.ModRecipeTypes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public interface IArcaneCraftingRecipe extends ICraftingRecipe {

    int getMystic(CraftingInventory inv);

    @Override
    default IRecipeType<?> getType() {
        return ModRecipeTypes.ARCANE_CRAFTING_TYPE;
    }
}
