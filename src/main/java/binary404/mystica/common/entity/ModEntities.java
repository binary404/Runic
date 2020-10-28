package binary404.mystica.common.entity;

import binary404.mystica.Mystica;
import binary404.mystica.common.entity.hostile.EntityBeholder;
import binary404.mystica.common.entity.hostile.EntityCultZombie;
import binary404.mystica.common.entity.hostile.EntityRangedCultZombie;
import binary404.mystica.common.entity.taint.EntityTaintCloud;
import binary404.mystica.common.entity.weave.EntityWeaveThread;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    @ObjectHolder("mystica:cult_zombie")
    public static EntityType<EntityCultZombie> CULT_ZOMBIE;

    @ObjectHolder("mystica:ranged_cult_zombie")
    public static EntityType<EntityRangedCultZombie> RANGED_CULT_ZOMBIE;

    @ObjectHolder("mystica:cult_orb")
    public static EntityType<EntityCultOrb> CULT_ORB;

    @ObjectHolder("mystica:beholder")
    public static EntityType<EntityBeholder> BEHOLDER;

    @ObjectHolder("mystica:thread")
    public static EntityType<EntityWeaveThread> THREAD;

    @ObjectHolder("mystica:taint_cloud")
    public static EntityType<EntityTaintCloud> TAINT_CLOUD;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
        event.getRegistry().register(EntityType.Builder.<EntityCultZombie>create(EntityCultZombie::new, EntityClassification.MONSTER).size(0.6F, 1.95F).build("").setRegistryName("mystica:cult_zombie"));
        event.getRegistry().register(EntityType.Builder.<EntityRangedCultZombie>create(EntityRangedCultZombie::new, EntityClassification.MONSTER).size(0.6F, 1.95F).build("").setRegistryName("mystica:ranged_cult_zombie"));
        event.getRegistry().register(EntityType.Builder.<EntityCultOrb>create(EntityCultOrb::new, EntityClassification.MISC).size(0.6F, 0.6F).setCustomClientFactory(((spawnEntity, world) -> new EntityCultOrb(world))).build("").setRegistryName("mystica:cult_orb"));
        event.getRegistry().register(EntityType.Builder.<EntityBeholder>create(EntityBeholder::new, EntityClassification.MONSTER).size(1.0F, 1.0F).build("").setRegistryName("mystica:beholder"));
        event.getRegistry().register(EntityType.Builder.<EntityWeaveThread>create(EntityWeaveThread::new, EntityClassification.MISC).size(0.1F, 0.1F).build("").setRegistryName("mystica:thread"));
        event.getRegistry().register(EntityType.Builder.<EntityTaintCloud>create(EntityTaintCloud::new, EntityClassification.MISC).size(1.0F, 1.0F).build("").setRegistryName("mystica:taint_cloud"));
    }

}
