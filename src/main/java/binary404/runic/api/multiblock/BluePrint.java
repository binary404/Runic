package binary404.runic.api.multiblock;

import binary404.runic.api.internal.IRunicRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BluePrint implements IRunicRecipe {

    Part[][][] parts;
    String research;
    ItemStack displayStack;
    ItemStack[] ingredientList;
    private String group;

    public BluePrint(String research, Part[][][] parts, ItemStack... ingredientList) {
        this.parts = parts;
        this.research = research;
        this.ingredientList = ingredientList;
    }

    public BluePrint(String research, ItemStack display, Part[][][] parts, ItemStack... ingredientList) {
        this.parts = parts;
        this.research = research;
        this.displayStack = display;
        this.ingredientList = ingredientList;
    }


    public Part[][][] getParts() {
        return this.parts;
    }


    public String getResearch() {
        return this.research;
    }


    public ItemStack[] getIngredientList() {
        return this.ingredientList;
    }


    public ItemStack getDisplayStack() {
        return this.displayStack;
    }


    public String getGroup() {
        return this.group;
    }


    public BluePrint setGroup(ResourceLocation loc) {
        this.group = loc.toString();
        return this;
    }

}
