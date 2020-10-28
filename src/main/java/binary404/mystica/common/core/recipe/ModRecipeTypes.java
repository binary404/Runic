package binary404.mystica.common.core.recipe;

import binary404.mystica.Mystica;
import binary404.mystica.api.recipe.IArcaneCraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModRecipeTypes {

    public static final IRecipeType<IArcaneCraftingRecipe> ARCANE_CRAFTING_TYPE = new RecipeType<>();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        ResourceLocation id = Mystica.key("arcane_crafting");
        Registry.register(Registry.RECIPE_TYPE, id, ARCANE_CRAFTING_TYPE);
        event.getRegistry().register(ShapedArcaneRecipe.SERIALIZER.setRegistryName(id));
    }

    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }

}
