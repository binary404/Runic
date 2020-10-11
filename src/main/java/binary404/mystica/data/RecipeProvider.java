package binary404.mystica.data;

import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.items.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

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
        ShapedRecipeBuilder.shapedRecipe(ModItems.mysticometer)
                .key('I', Items.IRON_INGOT)
                .key('G', Blocks.GLASS)
                .key('D', Items.DIAMOND)
                .patternLine("III")
                .patternLine("IGI")
                .patternLine(" D ")
                .addCriterion("has_item", hasItem(Items.IRON_INGOT))
                .build(consumer);
    }
}
