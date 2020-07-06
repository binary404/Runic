package binary404.runic.common.config;

import binary404.runic.api.RunicApi;
import binary404.runic.api.multiblock.BluePrint;
import binary404.runic.api.multiblock.Part;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeConfig {

    public static HashMap<String, ArrayList<ResourceLocation>> recipeGroups = new HashMap();

    public static void init() {
        initCompoundRecipes();
    }

    public static void initCompoundRecipes() {
        Part FURNACE = new Part(Blocks.FURNACE, new ItemStack(Blocks.FURNACE));
        Part ANVIL = new Part(Blocks.ANVIL, new ItemStack(Blocks.ANVIL));
        Part OBSIDION = new Part(Blocks.OBSIDIAN, new ItemStack(Blocks.OBSIDIAN));
        Part[][][] infernalFurnaceBlueprint = {{{null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}}, {{null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {OBSIDION, null, OBSIDION, null, OBSIDION}}, {{FURNACE, null, FURNACE, null, FURNACE}, {null, null, null, null, null}, {null, null, ANVIL, null, null}, {null, null, null, null, null}, {OBSIDION, null, OBSIDION, null, OBSIDION}}};

        RunicApi.addMultiblockRecipeToCatalog(new ResourceLocation("runic:test"), new BluePrint("FIRST_STEPS", infernalFurnaceBlueprint, new ItemStack[]{new ItemStack(Blocks.NETHER_BRICKS, 12), new ItemStack(Blocks.OBSIDIAN, 12), new ItemStack(Blocks.IRON_BARS), new ItemStack(Items.LAVA_BUCKET)}));
    }

}
