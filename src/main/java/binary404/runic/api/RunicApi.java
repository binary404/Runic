package binary404.runic.api;

import binary404.runic.api.internal.CommonInternals;
import binary404.runic.api.internal.IRunicRecipe;
import binary404.runic.api.multiblock.BluePrint;
import net.minecraft.util.ResourceLocation;

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

}
