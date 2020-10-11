package binary404.mystica.api.research;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class ResearchStage {
    String text;
    ResourceLocation[] recipes;
    Object[] obtain;
    Object[] craft;
    int[] craftReference;
    Point[] points;
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

    public Point[] getPoints() {
        return points;
    }

    public void setCraftReference(int[] craftReference) {
        this.craftReference = craftReference;
    }

    public void setPoints(Point[] points) {
        this.points = points;
    }

    public int getWarp() {
        return warp;
    }

    public void setWarp(int warp) {
        this.warp = warp;
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

    public static class Point {
        public int amount;
        public ResearchCategory category;

        public Point(ResearchCategory category, int num) {
            this.amount = num;
            this.category = category;
        }

        public static Point parse(String text) {
            String[] s = text.split(";");
            if (s.length == 2) {
                int num = 0;
                try {
                    num = Integer.parseInt(s[1]);
                } catch (Exception e) {
                }
                ResearchCategory f = ResearchCategories.getResearchCategory(s[0].toUpperCase());
                if (f != null && num > 0) {
                    return new Point(f, num);
                }
            }
            return null;
        }
    }
}
