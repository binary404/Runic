package binary404.runic.common.config;

import binary404.runic.api.RunicApi;
import binary404.runic.api.research.ResearchCategories;
import net.minecraft.util.ResourceLocation;

public class ResearchConfig {

    public static String[] runicCategories = new String[]{"BASICS", "RUNES"};

    public static void init() {
        ResearchConfig.initCategories();
        for (String cat : runicCategories) {
            RunicApi.registerResearchLocation(new ResourceLocation("runic", "research/" + cat.toLowerCase()));
        }
    }

    public static void post() {
        ResearchManager.parseAllResearch();
    }

    private static void initCategories() {
        ResearchCategories.registerCategory("BASICS", null, new ResourceLocation("runic", "textures/item/guide.png"));
        ResearchCategories.registerCategory("RUNES", "UNLOCK_RUNES", new ResourceLocation("runic", "textures/item/blank_rune.png"));
    }

}
