package binary404.runic.data;

import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {

    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        registerMain(consumer);
    }

    private void registerMain(Consumer<IFinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapelessRecipe(ModBlocks.runed_stone)
                .addIngredient(Blocks.CHISELED_STONE_BRICKS)
                .addIngredient(Blocks.CHISELED_STONE_BRICKS)
                .addIngredient(Blocks.CHISELED_STONE_BRICKS)
                .addIngredient(Blocks.CHISELED_STONE_BRICKS)
                .addIngredient(Blocks.CHISELED_STONE_BRICKS)
                .setGroup("runic:runed_stone")
                .addCriterion("has_item", hasItem(ModBlocks.runed_stone))
                .build(consumer);
        ShapedRecipeBuilder.shapedRecipe(ModBlocks.chisel)
                .key('P', Blocks.PISTON)
                .key('G', Items.GOLD_INGOT)
                .key('R', Blocks.REDSTONE_BLOCK)
                .key('S', ModBlocks.runed_stone)
                .patternLine("PGP")
                .patternLine("SRS")
                .patternLine("SSS")
                .addCriterion("has_item", hasItem(ModBlocks.chisel))
                .build(consumer);
    }
}
