package binary404.mystica.api.research;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

import java.util.Arrays;

public class ResearchEntry {
    String key;
    String category;
    String name;
    String[] parents;
    String[] siblings;
    int displayColumn;
    int displayRow;
    Object[] icons;
    EnumResearchMeta[] meta;
    ItemStack[] rewardItem;
    ResearchStage.Point[] rewardPoints;
    ResearchStage[] stages;
    ResearchAddendum[] addenda;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public String getLocalizedName() {
        return I18n.format((String) this.getName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getParents() {
        return this.parents;
    }

    public String[] getParentsClean() {
        String[] out = null;
        if (this.parents != null) {
            out = this.getParentsStripped();
            for (int q = 0; q < out.length; ++q) {
                if (!out[q].contains("@")) continue;
                out[q] = out[q].substring(0, out[q].indexOf("@"));
            }
        }
        return out;
    }

    public String[] getParentsStripped() {
        String[] out = null;
        if (this.parents != null) {
            out = new String[this.parents.length];
            for (int q = 0; q < out.length; ++q) {
                out[q] = "" + this.parents[q];
                if (!out[q].startsWith("~")) continue;
                out[q] = out[q].substring(1);
            }
        }
        return out;
    }

    public void setParents(String[] parents) {
        this.parents = parents;
    }

    public String[] getSiblings() {
        return this.siblings;
    }

    public void setSiblings(String[] siblings) {
        this.siblings = siblings;
    }

    public int getDisplayColumn() {
        return this.displayColumn;
    }

    public void setDisplayColumn(int displayColumn) {
        this.displayColumn = displayColumn;
    }

    public int getDisplayRow() {
        return this.displayRow;
    }

    public void setDisplayRow(int displayRow) {
        this.displayRow = displayRow;
    }

    public Object[] getIcons() {
        return this.icons;
    }

    public void setIcons(Object[] icons) {
        this.icons = icons;
    }

    public EnumResearchMeta[] getMeta() {
        return this.meta;
    }

    public boolean hasMeta(EnumResearchMeta me) {
        return this.meta == null ? false : Arrays.asList(this.meta).contains((Object) me);
    }

    public void setMeta(EnumResearchMeta[] meta) {
        this.meta = meta;
    }

    public ResearchStage[] getStages() {
        return this.stages;
    }

    public void setStages(ResearchStage[] stages) {
        this.stages = stages;
    }

    public ItemStack[] getRewardItem() {
        return this.rewardItem;
    }

    public void setRewardItem(ItemStack[] rewardItem) {
        this.rewardItem = rewardItem;
    }

    public ResearchStage.Point[] getRewardPoints() {
        return rewardPoints;
    }

    public void setRewardPoints(ResearchStage.Point[] rewardPoints) {
        this.rewardPoints = rewardPoints;
    }

    public ResearchAddendum[] getAddenda() {
        return this.addenda;
    }

    public void setAddenda(ResearchAddendum[] addenda) {
        this.addenda = addenda;
    }

    public static enum EnumResearchMeta {
        ROUND,
        SPIKY,
        REVERSE,
        HIDDEN,
        AUTOUNLOCK,
        HEX;
    }
}
