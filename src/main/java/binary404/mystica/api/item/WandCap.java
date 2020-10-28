package binary404.mystica.api.item;

import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;

public class WandCap {

    private String tag;
    private int craftCost;
    float baseCostModifier;
    int chargeBonus;
    int color;
    ItemStack item;

    public static LinkedHashMap<String, WandCap> caps = new LinkedHashMap<>();

    public WandCap(String tag, float discount, int charge, ItemStack item, int craftCost, int color) {
        setTag(tag);
        setBaseCostModifier(discount);
        setColor(color);
        setItem(item);
        setChargeBonus(charge);
        setCraftCost(craftCost);
        caps.put(tag, this);
    }

    public String getTag() {
        return tag;
    }

    public int getCraftCost() {
        return craftCost;
    }

    public float getBaseCostModifier() {
        return baseCostModifier;
    }

    public int getChargeBonus() {
        return chargeBonus;
    }

    public int getColor() {
        return color;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setCraftCost(int craftCost) {
        this.craftCost = craftCost;
    }

    public void setBaseCostModifier(float baseCostModifier) {
        this.baseCostModifier = baseCostModifier;
    }

    public void setChargeBonus(int chargeBonus) {
        this.chargeBonus = chargeBonus;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }
}
