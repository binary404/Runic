package binary404.runic.common.config;

import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeConfig {

    public static HashMap<String, ArrayList<ResourceLocation>> recipeGroups = new HashMap();

    public static net.minecraft.item.crafting.RecipeManager manager = new net.minecraft.item.crafting.RecipeManager();

}
