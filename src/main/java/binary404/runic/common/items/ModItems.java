package binary404.runic.common.items;

import binary404.runic.Runic;
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

    @ObjectHolder("runic:forge_hammer")
    public static Item forge_hammer;

    @ObjectHolder("runic:blank_rune")
    public static Item blank_rune;

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(RunicTab.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        register(r, new ItemGuide(), "guide");

        register(r, new Item(defaultBuilder()), "blank_rune");

        register(r, new Item(defaultBuilder()), "forge_hammer");
    }

}
