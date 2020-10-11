package binary404.mystica.common.config;

import binary404.mystica.api.MysticaApi;
import binary404.mystica.api.multiblock.BluePrint;
import binary404.mystica.api.multiblock.IMultiBlockTrigger;
import binary404.mystica.api.multiblock.MultiBlockTrigger;
import binary404.mystica.api.multiblock.MultiBlockComponent;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.items.ModItems;
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
        /*MultiBlockComponent FURNACE = new MultiBlockComponent(Blocks.FURNACE, Blocks.AIR);
        MultiBlockComponent ANVIL = new MultiBlockComponent(Blocks.ANVIL, ModBlocks.arcane_anvil);
        MultiBlockComponent OBSIDIAN = new MultiBlockComponent(Blocks.OBSIDIAN, Blocks.AIR);
        MultiBlockComponent BOTTOM = new MultiBlockComponent(Blocks.OBSIDIAN, ModBlocks.storage);
        MultiBlockComponent OBSIDIAN_FURNACE = new MultiBlockComponent(Blocks.OBSIDIAN, ModBlocks.furnace);
        MultiBlockComponent[][][] arcane_furnace = {{{null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}}, {{FURNACE, null, FURNACE, null, FURNACE}, {null, null, null, null, null}, {null, null, null, null, null}, {null, null, null, null, null}, {OBSIDIAN, null, OBSIDIAN, null, OBSIDIAN}}, {{OBSIDIAN_FURNACE, null, OBSIDIAN_FURNACE, null, OBSIDIAN_FURNACE}, {null, null, null, null, null}, {null, null, ANVIL, null, null}, {null, null, null, null, null}, {BOTTOM, null, BOTTOM, null, BOTTOM}}};
        IMultiBlockTrigger.triggers.add(new MultiBlockTrigger("FIRST_STEPS", arcane_furnace));
        MysticaApi.addMultiblockRecipeToCatalog(new ResourceLocation("mystica:arcane_forge"), new BluePrint("FIRST_STEPS", arcane_furnace, new ItemStack[]{new ItemStack(Blocks.ANVIL, 1), new ItemStack(Blocks.OBSIDIAN, 9), new ItemStack(Blocks.FURNACE, 3)}));
         */
    }
}
