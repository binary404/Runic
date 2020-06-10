package binary404.runic.api.research;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.LinkedHashMap;

public class ResearchCategories {
    public static LinkedHashMap<String, ResearchCategory> researchCategories = new LinkedHashMap();

    public static ResearchCategory getResearchCategory(String key) {
        return researchCategories.get(key);
    }

    public static String getCategoryName(String key) {
        return I18n.format((String) ("demonic.research_category." + key));
    }

    public static ResearchEntry getResearch(String key) {
        Collection<ResearchCategory> rc = researchCategories.values();
        for (ResearchCategory cat : rc) {
            Collection<ResearchEntry> rl = cat.research.values();
            for (ResearchEntry ri : rl) {
                if (!ri.key.equals(key)) continue;
                return ri;
            }
        }
        return null;
    }

    public static ResearchCategory registerCategory(String key, String researchkey, ResourceLocation icon, ResourceLocation background) {
        if (ResearchCategories.getResearchCategory(key) == null) {
            ResearchCategory rl = new ResearchCategory(key, researchkey, icon, background);
            researchCategories.put(key, rl);
            return rl;
        }
        return null;
    }

    public static ResearchCategory registerCategory(String key, String researchkey, ResourceLocation icon, ResourceLocation background, ResourceLocation background2) {
        if (ResearchCategories.getResearchCategory(key) == null) {
            ResearchCategory rl = new ResearchCategory(key, researchkey, icon, background, background2);
            researchCategories.put(key, rl);
            return rl;
        }
        return null;
    }
}
