package binary404.mystica.api.research;

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
        return I18n.format((String) ("mystica.research_category." + key));
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

    public static ResearchCategory registerCategory(String key, String researchkey, ResourceLocation icon) {
        if (ResearchCategories.getResearchCategory(key) == null) {
            ResearchCategory rl = new ResearchCategory(key, researchkey, icon);
            researchCategories.put(key, rl);
            return rl;
        }
        return null;
    }
}
