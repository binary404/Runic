package binary404.runic.api.research;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ResearchAddendum {
    String text;
    ResourceLocation[] recipes;
    String[] research;

    public String getText() {
        return this.text;
    }

    public String getTextLocalized() {
        return I18n.format((String) this.getText());
    }

    public void setText(String text) {
        this.text = text;
    }

    public ResourceLocation[] getRecipes() {
        return this.recipes;
    }

    public void setRecipes(ResourceLocation[] recipes) {
        this.recipes = recipes;
    }

    public String[] getResearch() {
        return this.research;
    }

    public void setResearch(String[] research) {
        this.research = research;
    }
}
