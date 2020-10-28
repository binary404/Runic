package binary404.mystica.data;

import binary404.mystica.Mystica;
import binary404.mystica.common.core.recipe.ShapedArcaneRecipeBuilder;
import binary404.mystica.common.items.ModItems;
import binary404.mystica.data.recipe.WandRecipe;
import net.minecraft.block.Blocks;
import net.minecraft.data.*;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

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

        specialRecipe(consumer, WandRecipe.SERIALIZER);

        ShapedArcaneRecipeBuilder.arcaneRecipe(ModItems.mysticometer)
                .key('I', Items.IRON_INGOT)
                .key('G', Blocks.GLASS)
                .key('D', Items.DIAMOND)
                .patternLine("III")
                .patternLine("IGI")
                .patternLine(" D ")
                .mysticCost(10)
                .build(consumer);
    }

    private void specialRecipe(Consumer<IFinishedRecipe> consumer, SpecialRecipeSerializer<?> serializer) {
        ResourceLocation name = Registry.RECIPE_SERIALIZER.getKey(serializer);
        CustomRecipeBuilder.customRecipe(serializer).build(consumer, Mystica.key("dynamic/" + name.getPath()).toString());
    }
}
