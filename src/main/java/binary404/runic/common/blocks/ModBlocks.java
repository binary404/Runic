package binary404.runic.common.blocks;

import binary404.runic.Runic;
import binary404.runic.common.blocks.machine.BlockArcaneForge;
import binary404.runic.common.blocks.machine.BlockArcaneForgeFurnace;
import binary404.runic.common.blocks.machine.BlockStorage;
import binary404.runic.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.runic.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    @ObjectHolder("runic:runed_stone")
    public static Block runed_stone;

    @ObjectHolder("runic:dungeon_core")
    public static BlockDungeonCore dungeon_core;

    @ObjectHolder("runic:mob_crystal")
    public static Block mob_crystal;

    @ObjectHolder("runic:arcane_anvil")
    public static Block arcane_anvil;

    @ObjectHolder("runic:storage")
    public static Block storage;

    @ObjectHolder("runic:furnace")
    public static Block furnace;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> r = event.getRegistry();

        Block.Properties builder = Block.Properties.create(Material.ROCK).hardnessAndResistance(5.0F, 10.0F).lightValue(7);

        register(r, new Block(builder), "runed_stone");
        register(r, new BlockStorage(builder), "storage");
        register(r, new BlockArcaneForgeFurnace(builder), "furnace");
        builder = Block.Properties.create(Material.GLASS).lightValue(11).hardnessAndResistance(1.0F, 199000.0F);
        register(r, new BlockDungeonCore(builder), "dungeon_core");
        register(r, new BlockArcaneForge(Block.Properties.create(Material.ANVIL, MaterialColor.IRON).hardnessAndResistance(5.0F, 1200.0F).notSolid()), "arcane_anvil");
        builder = builder.notSolid();
        register(r, new BlockMobCrystal(builder), "mob_crystal");
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        register(r, new BlockItem(runed_stone, ModItems.defaultBuilder()), "runed_stone");
        register(r, new BlockItem(storage, ModItems.defaultBuilder()), "storage");
        register(r, new BlockItem(furnace, ModItems.defaultBuilder()), "furnace");
        register(r, new BlockItem(dungeon_core, ModItems.defaultBuilder()), "dungeon_core");
        register(r, new BlockItem(arcane_anvil, ModItems.defaultBuilder()), "arcane_anvil");
        register(r, new BlockItem(mob_crystal, ModItems.defaultBuilder()), "mob_crystal");
    }

}
