package binary404.runic.api;

import binary404.runic.api.internal.CommonInternals;
import net.minecraft.util.ResourceLocation;

public class RunicApi {

    public static void registerResearchLocation(ResourceLocation loc) {
        if (!CommonInternals.jsonLocs.containsKey(loc.toString())) {
            CommonInternals.jsonLocs.put(loc.toString(), loc);
        }
    }

}
