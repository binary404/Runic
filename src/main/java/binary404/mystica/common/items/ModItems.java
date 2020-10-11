package binary404.mystica.common.items;

import binary404.mystica.Mystica;
import binary404.mystica.common.core.MysticaTab;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item guide;

    public static Item mysticometer;

    public static Item goggles;

    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(MysticaTab.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> r = event.getRegistry();

        guide = register(r, new ItemGuide(), "guide");
        mysticometer = register(r, new ItemMysticometer(), "mysticometer");
        goggles = register(r, new ItemGoggles(), "goggles");
    }

}
