package binary404.runic.api.research;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ResearchCategory {
    public int minDisplayColumn;
    public int minDisplayRow;
    public int maxDisplayColumn;
    public int maxDisplayRow;
    public ResourceLocation icon;
    public ResourceLocation background;
    public ResourceLocation background2;
    public String researchKey;
    public String key;
    public Map<String, ResearchEntry> research = new HashMap<String, ResearchEntry>();

    public ResearchCategory(String key, String researchkey, ResourceLocation icon, ResourceLocation background) {
        this.key = key;
        this.researchKey = researchkey;
        this.icon = icon;
        this.background = background;
        this.background2 = null;
    }

    public ResearchCategory(String key, String researchKey, ResourceLocation icon, ResourceLocation background, ResourceLocation background2) {
        this.key = key;
        this.researchKey = researchKey;
        this.icon = icon;
        this.background = background;
        this.background2 = background2;
    }
}
