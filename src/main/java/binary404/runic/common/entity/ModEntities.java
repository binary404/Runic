package binary404.runic.common.entity;

import binary404.runic.Runic;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    @ObjectHolder("runic:special_item")
    public static EntityType<EntitySpecialItem> SPECIAL_ITEM;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(EntityType.Builder.<EntitySpecialItem>create(EntitySpecialItem::new, EntityClassification.MISC).size(0.25F, 0.25F).build("").setRegistryName("runic:special_item"));
    }

}
