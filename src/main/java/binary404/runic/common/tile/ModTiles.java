package binary404.runic.common.tile;

import binary404.runic.Runic;
import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.tile.world.TileDungeonCore;
import binary404.runic.common.tile.world.TileMobCrystal;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.runic.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    @ObjectHolder("runic:dungeon_core")
    public static TileEntityType<TileDungeonCore> DUNGEON_CORE;

    @ObjectHolder("runic:mob_crystal")
    public static TileEntityType<TileMobCrystal> MOB_CRYSTAL;

    @ObjectHolder("runic:arcane_forge")
    public static TileEntityType<TileArcaneForge> ARCANE_FORGE;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        register(r, TileEntityType.Builder.create(TileDungeonCore::new, ModBlocks.dungeon_core).build(null), "dungeon_core");
        register(r, TileEntityType.Builder.create(TileMobCrystal::new, ModBlocks.mob_crystal).build(null), "mob_crystal");
        register(r, TileEntityType.Builder.create(TileArcaneForge::new, ModBlocks.arcane_anvil).build(null), "arcane_forge");
    }

}
