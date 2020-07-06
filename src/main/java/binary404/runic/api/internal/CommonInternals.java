package binary404.runic.api.internal;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class CommonInternals {

    public static HashMap<String, ResourceLocation> jsonLocs = new HashMap();
    public static HashMap<ResourceLocation, IRunicRecipe> craftingRecipeCatalog = new HashMap();

    public static IRunicRecipe getCatalogRecipe(ResourceLocation key) {
        return craftingRecipeCatalog.get((Object) key);
    }

}
