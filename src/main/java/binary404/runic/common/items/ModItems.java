package binary404.runic.common.items;

import binary404.runic.Runic;
import binary404.runic.common.blocks.fluid.ItemSolventBucket;
import binary404.runic.common.blocks.fluid.RegistryFluids;
import binary404.runic.common.core.RunicTab;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.runic.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    @ObjectHolder("runic:guide")
    public static ItemGuide guide;

    @ObjectHolder("runic:basic_mold")
    public static Item basic_mold;

    @ObjectHolder("runic:solvent_bucket")
    public static ItemSolventBucket BUCKET_SOLVENT = null;

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(RunicTab.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        register(r, new ItemGuide(), "guide");

        register(r, new ItemMold(defaultBuilder()), "basic_mold");

        register(r, new ItemSolventBucket(() -> RegistryFluids.SOLVENT_SOURCE), "solvent_bucket");
    }

}
