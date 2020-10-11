package binary404.mystica.api.multiblock;

import binary404.mystica.api.internal.IMysticaRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class BluePrint implements IMysticaRecipe {

    MultiBlockComponent[][][] multiBlockComponents;
    String research;
    ItemStack displayStack;
    ItemStack[] ingredientList;
    private String group;

    public BluePrint(String research, MultiBlockComponent[][][] multiBlockComponents, ItemStack... ingredientList) {
        this.multiBlockComponents = multiBlockComponents;
        this.research = research;
        this.ingredientList = ingredientList;
    }

    public BluePrint(String research, ItemStack display, MultiBlockComponent[][][] multiBlockComponents, ItemStack... ingredientList) {
        this.multiBlockComponents = multiBlockComponents;
        this.research = research;
        this.displayStack = display;
        this.ingredientList = ingredientList;
    }


    public MultiBlockComponent[][][] getMultiBlockComponents() {
        return this.multiBlockComponents;
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
