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

    @ObjectHolder("runic:cult_zombie")
    public static EntityType<EntityCultZombie> CULT_ZOMBIE;

    @ObjectHolder("runic:ranged_cult_zombie")
    public static EntityType<EntityRangedCultZombie> RANGED_CULT_ZOMBIE;

    @ObjectHolder("runic:cult_orb")
    public static EntityType<EntityCultOrb> CULT_ORB;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(EntityType.Builder.<EntitySpecialItem>create(EntitySpecialItem::new, EntityClassification.MISC).size(0.25F, 0.25F).build("").setRegistryName("runic:special_item"));
        event.getRegistry().register(EntityType.Builder.<EntityCultZombie>create(EntityCultZombie::new, EntityClassification.MONSTER).size(0.6F, 1.95F).build("").setRegistryName("runic:cult_zombie"));
        event.getRegistry().register(EntityType.Builder.<EntityRangedCultZombie>create(EntityRangedCultZombie::new, EntityClassification.MONSTER).size(0.6F, 1.95F).build("").setRegistryName("runic:ranged_cult_zombie"));
        event.getRegistry().register(EntityType.Builder.<EntityCultOrb>create(EntityCultOrb::new, EntityClassification.MISC).size(0.6F, 0.6F).setCustomClientFactory(((spawnEntity, world) -> new EntityCultOrb(world))).build("").setRegistryName("runic:cult_orb"));
    }

}
