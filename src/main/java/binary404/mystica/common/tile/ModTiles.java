package binary404.mystica.common.tile;

import binary404.mystica.Mystica;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.tile.world.TileDungeonCore;
import binary404.mystica.common.tile.world.TileMobCrystal;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    @ObjectHolder("mystica:dungeon_core")
    public static TileEntityType<TileDungeonCore> DUNGEON_CORE;

    @ObjectHolder("mystica:mob_crystal")
    public static TileEntityType<TileMobCrystal> MOB_CRYSTAL;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        register(r, TileEntityType.Builder.create(TileDungeonCore::new, ModBlocks.dungeon_core).build(null), "dungeon_core");
        register(r, TileEntityType.Builder.create(TileMobCrystal::new, ModBlocks.mob_crystal).build(null), "mob_crystal");
    }
}