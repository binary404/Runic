package binary404.mystica.api.item;

import net.minecraft.item.ItemStack;

import java.util.LinkedHashMap;

public class WandRod {

    private String tag;
    private int craftCost;
    ItemStack item;
    int capacity;
    int color;

    public static LinkedHashMap<String, WandRod> rods = new LinkedHashMap<>();

    public static WandRod getWandRod(String tag) {
        return rods.get(tag);
    }

    public WandRod(String tag, int capacity, ItemStack item, int craftCost, int color) {
        this.capacity = capacity;
        this.item = item;
        this.color = color;
        setTag(tag);
        setCraftCost(craftCost);
        rods.put(tag, this);
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCapacity() {
        return this.capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getCraftCost() {
        return craftCost;
    }

    public void setCraftCost(int craftCost) {
        this.craftCost = craftCost;
    }
}
