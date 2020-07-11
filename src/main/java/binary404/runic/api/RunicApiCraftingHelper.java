package binary404.runic.api;

import binary404.runic.api.recipe.ForgeRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class RunicApiCraftingHelper {

    public static ForgeRecipe findForgeRecipe(ArrayList<ItemStack> items, ItemStack input) {
        for (Object recipe : RunicApi.getCraftingRecipes().values()) {
            if (recipe != null && recipe instanceof ForgeRecipe && ((ForgeRecipe) recipe).matches(items, input)) {
                return (ForgeRecipe) recipe;
            }
        }
        return null;
    }

}
