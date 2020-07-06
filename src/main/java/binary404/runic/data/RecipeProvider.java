package binary404.runic.data;

import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.items.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

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
        ShapedRecipeBuilder.shapedRecipe(ModItems.blank_rune)
                .patternLine("RRR")
                .patternLine("RLR")
                .patternLine("RRR")
                .key('R', ModBlocks.runed_stone)
                .key('L', Items.LAPIS_LAZULI)
                .addCriterion("has_item", hasItem(ModBlocks.runed_stone))
                .build(consumer, new ResourceLocation("runic:blank_rune"));
    }
}
