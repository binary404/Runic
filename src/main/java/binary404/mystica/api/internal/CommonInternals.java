package binary404.mystica.api.internal;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;

public class CommonInternals {

    public static HashMap<String, ResourceLocation> jsonLocs = new HashMap();
    public static HashMap<ResourceLocation, IMysticaRecipe> craftingRecipeCatalog = new HashMap();

    public static IMysticaRecipe getCatalogRecipe(ResourceLocation key) {
        return craftingRecipeCatalog.get((Object) key);
    }

}
