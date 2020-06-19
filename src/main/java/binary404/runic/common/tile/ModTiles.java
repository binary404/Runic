package binary404.runic.common.tile;

import binary404.runic.Runic;
import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.runic.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTiles {

    @ObjectHolder("runic:chisel")
    public static TileEntityType<TileChisel> CHISEL;

    @ObjectHolder("runic:rune_molder")
    public static TileEntityType<TileRuneMolder> MOLDER;

    @SubscribeEvent
    public static void registerTileEntity(RegistryEvent.Register<TileEntityType<?>> event) {
        IForgeRegistry<TileEntityType<?>> r = event.getRegistry();
        register(r, TileEntityType.Builder.create(TileChisel::new, ModBlocks.chisel).build(null), "chisel");
        register(r, TileEntityType.Builder.create(TileRuneMolder::new, ModBlocks.rune_molder).build(null), "rune_molder");
    }

}
