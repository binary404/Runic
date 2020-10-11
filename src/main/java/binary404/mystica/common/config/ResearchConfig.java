package binary404.mystica.common.config;

import binary404.mystica.api.MysticaApi;
import binary404.mystica.api.research.ResearchCategories;
import net.minecraft.util.ResourceLocation;

public class ResearchConfig {

    public static String[] mysticaCategories = new String[]{"BASICS"};

    public static void init() {
        ResearchConfig.initCategories();
        for (String cat : mysticaCategories) {
            MysticaApi.registerResearchLocation(new ResourceLocation("mystica", "research/" + cat.toLowerCase()));
        }
    }

    public static void post() {
        ResearchManager.parseAllResearch();
    }

    private static void initCategories() {
        ResearchCategories.registerCategory("BASICS", null, new ResourceLocation("mystica", "textures/item/guide.png"));
    }

}
