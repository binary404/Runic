package binary404.runic.api;

import binary404.runic.api.internal.CommonInternals;
import binary404.runic.api.internal.IRunicRecipe;
import binary404.runic.api.multiblock.BluePrint;
import binary404.runic.api.recipe.ForgeRecipe;
import com.google.gson.JsonElement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.NBTIngredient;

import java.util.HashMap;

public class RunicApi {

    public static void registerResearchLocation(ResourceLocation loc) {
        if (!CommonInternals.jsonLocs.containsKey(loc.toString())) {
            CommonInternals.jsonLocs.put(loc.toString(), loc);
        }
    }

    public static HashMap<ResourceLocation, IRunicRecipe> getCraftingRecipes() {
        return CommonInternals.craftingRecipeCatalog;
    }

    public static void addMultiblockRecipeToCatalog(ResourceLocation registry, BluePrint recipe) {
        getCraftingRecipes().put(registry, recipe);
    }

    public static void addForgeRecipeToCatalog(ResourceLocation registry, ForgeRecipe recipe) {
        getCraftingRecipes().put(registry, recipe);
    }

    public static Ingredient getIngredient(Object obj) {
        if (obj instanceof Ingredient)
            return (Ingredient) obj;
        else if (obj instanceof ItemStack)
            return Ingredient.fromStacks(((ItemStack) obj).copy());
        else if (obj instanceof Item)
            return Ingredient.fromItems((Item) obj);
        else if (obj instanceof Block)
            return Ingredient.fromStacks(new ItemStack((Block) obj, 1));
        else if (obj instanceof JsonElement)
            throw new IllegalArgumentException("JsonObjects must use getIngredient(JsonObject, JsonContext)");

        return null;
    }

}
