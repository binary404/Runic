package binary404.mystica.common.blocks;

import binary404.mystica.Mystica;
import binary404.mystica.common.blocks.world.taint.BlockTaint;
import binary404.mystica.common.blocks.world.taint.BlockTaintFiber;
import binary404.mystica.common.blocks.world.taint.BlockTaintLog;
import binary404.mystica.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    @ObjectHolder("mystica:runed_stone")
    public static Block runed_stone;

    @ObjectHolder("mystica:dungeon_core")
    public static BlockDungeonCore dungeon_core;

    @ObjectHolder("mystica:mob_crystal")
    public static Block mob_crystal;

    @ObjectHolder("mystica:taint_fiber")
    public static Block taint_fiber;

    @ObjectHolder("mystica:taint_log")
    public static Block taint_log;

    @ObjectHolder("mystica:taint_rock")
    public static Block taint_rock;

    @ObjectHolder("mystica:taint_soil")
    public static Block taint_soil;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 10.0F).setLightLevel((b) -> 7);

        register(r, new Block(builder), "runed_stone");

        register(r, new BlockTaintFiber(), "taint_fiber");
        register(r, new BlockTaintLog(), "taint_log");
        register(r, new BlockTaint(), "taint_rock");
        register(r, new BlockTaint(), "taint_soil");

        builder = Block.Properties.create(Material.GLASS).setLightLevel((n) -> 11).hardnessAndResistance(1.0F, 199000.0F);
        register(r, new BlockDungeonCore(builder), "dungeon_core");
        builder = builder.notSolid();
        register(r, new BlockMobCrystal(builder), "mob_crystal");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        register(r, new BlockItem(runed_stone, ModItems.defaultBuilder()), "runed_stone");
        register(r, new BlockItem(mob_crystal, ModItems.defaultBuilder()), "mob_crystal");
        register(r, new BlockItem(taint_fiber, ModItems.defaultBuilder()), "taint_fiber");
        register(r, new BlockItem(taint_log, ModItems.defaultBuilder()), "taint_log");
        register(r, new BlockItem(taint_rock, ModItems.defaultBuilder()), "taint_rock");
        register(r, new BlockItem(taint_soil, ModItems.defaultBuilder()), "taint_soil");
    }

}
