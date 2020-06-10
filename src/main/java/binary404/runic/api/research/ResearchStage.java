package binary404.runic.api.research;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ResearchStage {
    String text;
    ResourceLocation[] recipes;
    Object[] obtain;
    Object[] craft;
    int[] craftReference;
    String[] research;
    String[] researchIcon;
    int warp;

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

    public Object[] getObtain() {
        return this.obtain;
    }

    public void setObtain(Object[] obtain) {
        this.obtain = obtain;
    }

    public Object[] getCraft() {
        return this.craft;
    }

    public void setCraft(Object[] craft) {
        this.craft = craft;
    }

    public int[] getCraftReference() {
        return this.craftReference;
    }

    public void setCraftReference(int[] craftReference) {
        this.craftReference = craftReference;
    }

    public String[] getResearch() {
        return this.research;
    }

    public void setResearch(String[] research) {
        this.research = research;
    }

    public String[] getResearchIcon() {
        return this.researchIcon;
    }

    public void setResearchIcon(String[] research) {
        this.researchIcon = research;
    }

    public int getWarp() {
        return this.warp;
    }

    public void setWarp(int warp) {
        this.warp = warp;
    }
}
