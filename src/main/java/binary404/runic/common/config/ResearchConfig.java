package binary404.runic.common.config;

import binary404.runic.api.RunicApi;
import binary404.runic.api.research.ResearchCategories;
import net.minecraft.util.ResourceLocation;

public class ResearchConfig {

    public static String[] runicCategories = new String[]{"BASICS"};
    private static final ResourceLocation BACK_OVER = new ResourceLocation("runic", "textures/gui/gui_research_back_over.png");

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
        ResearchCategories.registerCategory("BASICS", null, new ResourceLocation("runic", "textures/items/book_necromancy.png"), new ResourceLocation("demonic", "textures/gui/gui_research_back_1.jpg"), BACK_OVER);
    }

}
