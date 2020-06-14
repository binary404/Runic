package binary404.runic.api.recipe;

import binary404.runic.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.ProjectileImpactEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class ChiselRecipes {

    public static ArrayList<ChiselRecipe> registry = new ArrayList<>();

    static ChiselRecipe stone = register(Blocks.STONE, ModItems.basic_mold);
    static ChiselRecipe obsidian = register(Blocks.OBSIDIAN, Items.DIAMOND);
    static ChiselRecipe gravel = register(Blocks.GRAVEL, Items.DIAMOND);

    public static ChiselRecipe register(Block input, Item output) {
        ChiselRecipe recipe = new ChiselRecipe(input, output);
        registry.add(recipe);
        return recipe;
    }

    public static class ChiselRecipe {

        public Block input;
        public Item output;

        public ChiselRecipe(Block input, Item output) {
            this.input = input;
            this.output = output;
        }

    }

}
