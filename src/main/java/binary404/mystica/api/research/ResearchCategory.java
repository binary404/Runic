package binary404.mystica.api.research;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ResearchCategory {
    public int minDisplayColumn;
    public int minDisplayRow;
    public int maxDisplayColumn;
    public int maxDisplayRow;
    public ResourceLocation icon;
    public String researchKey;
    public String key;
    public Map<String, ResearchEntry> research = new HashMap<String, ResearchEntry>();

    public ResearchCategory(String key, String researchkey, ResourceLocation icon) {
        this.key = key;
        this.researchKey = researchkey;
        this.icon = icon;
    }
}
