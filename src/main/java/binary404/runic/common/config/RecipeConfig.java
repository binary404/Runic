package binary404.runic.common.config;

import binary404.runic.api.RunicApi;
import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
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
        NonNullList<Ingredient> chisel = NonNullList.withSize(9, Ingredient.EMPTY);
        chisel.set(0, Ingredient.fromItems(Blocks.PISTON));
        chisel.set(1, Ingredient.fromItems(Items.GOLD_INGOT));
        chisel.set(2, Ingredient.fromItems(Blocks.PISTON));
        chisel.set(3, Ingredient.fromItems(ModBlocks.runed_stone));
        chisel.set(4, Ingredient.fromItems(Blocks.REDSTONE_BLOCK));
        chisel.set(5, Ingredient.fromItems(ModBlocks.runed_stone));
        chisel.set(6, Ingredient.fromItems(ModBlocks.runed_stone));
        chisel.set(7, Ingredient.fromItems(ModBlocks.runed_stone));
        chisel.set(8, Ingredient.fromItems(ModBlocks.runed_stone));
        RunicApi.addFakeCraftingRecipe(new ResourceLocation("runic", "chisel_fake"), new ShapedRecipe(new ResourceLocation("chisel_fake"), "", 3, 3, chisel, new ItemStack(ModBlocks.chisel)));
    }

}
