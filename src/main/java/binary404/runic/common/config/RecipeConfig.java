package binary404.runic.common.config;

import binary404.runic.api.RunicApi;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeConfig {

    public static HashMap<String, ArrayList<ResourceLocation>> recipeGroups = new HashMap();

    public static net.minecraft.item.crafting.RecipeManager manager = new net.minecraft.item.crafting.RecipeManager();

    public static void init() {
        initFakeRecipes();
    }

    public static void initFakeRecipes() {
        NonNullList<Ingredient> nonNullList = NonNullList.withSize(1, Ingredient.EMPTY);
        nonNullList.set(0, Ingredient.fromTag(ItemTags.LOGS));
        RunicApi.addFakeCraftingRecipe(new ResourceLocation("runic","plank_fake"), new ShapelessRecipe(new ResourceLocation("plank_fake"), "", new ItemStack(Items.OAK_PLANKS, 4), nonNullList));
    }

}
