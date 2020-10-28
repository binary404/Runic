package binary404.mystica.api.research;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ResearchAddendum {
    String text;
    String title;
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
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
