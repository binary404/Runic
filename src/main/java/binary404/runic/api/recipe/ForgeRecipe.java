package binary404.runic.api.recipe;

import binary404.runic.api.RunicApi;
import binary404.runic.api.internal.IRunicRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.util.RecipeMatcher;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ForgeRecipe implements IRunicRecipe {

    public String research;
    protected NonNullList<Ingredient> components;
    public Ingredient sourceInput;
    public ItemStack output;
    public int time;

    public ForgeRecipe(String research, ItemStack recipeResult, int time, Object catalyst, Object... recipe) {
        this.components = NonNullList.create();
        this.research = research;
        this.output = recipeResult;
        this.time = time;
        this.sourceInput = RunicApi.getIngredient(catalyst);
        if (this.sourceInput == null) {
            String exc = "Invalid central catalyst " + catalyst;
            throw new RuntimeException(exc);
        }
        for (Object in : recipe) {
            Ingredient ing = RunicApi.getIngredient(in);
            if (ing != null) {
                this.components.add(ing);
            } else {
                String ret = "Invalid recipe: ";
                for (Object tmp : recipe)
                    ret = ret + tmp + ", ";
                ret = ret + recipeResult;
                throw new RuntimeException(ret);
            }
        }
    }

    public boolean matches(List<ItemStack> input, ItemStack central) {
        if (getRecipeInput() == null)
            return false;
        return ((getRecipeInput() == Ingredient.EMPTY || getRecipeInput().test(central)) && RecipeMatcher.findMatches(input, getComponents()) != null);
    }

    public Ingredient getRecipeInput() {
        return this.sourceInput;
    }

    public NonNullList<Ingredient> getComponents() {
        return this.components;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public String getResearch() {
        return this.research;
    }
}
