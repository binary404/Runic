package binary404.runic.common.core.capability;

import binary404.runic.Runic;
import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.capability.IPlayerKnowledge;
import binary404.runic.common.config.ResearchManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CapabilityEvent {

    public static void init() {
        CapabilityManager.INSTANCE.register(IPlayerKnowledge.class, new PlayerKnowledgeStorage(), PlayerKnowledge::new);
    }

    @SubscribeEvent
    public static void cloneCapabilitiesEvent(PlayerEvent.Clone event) {
        try {
            CompoundNBT nbt = CapabilityHelper.getKnowledge(event.getOriginal()).serializeNBT();
            CapabilityHelper.getKnowledge(event.getPlayer()).deserializeNBT(nbt);
        } catch (Exception e) {
        }
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(PlayerKnowledgeProvider.NAME, new PlayerKnowledgeProvider());
        }
    }

    @SubscribeEvent
    public static void playerJoinSync(EntityJoinWorldEvent event) {
        if (!event.getWorld().isRemote && event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            IPlayerKnowledge pk = CapabilityHelper.getKnowledge(player);
            if (pk != null)
                pk.sync((ServerPlayerEntity) player);
        }
    }

    @SubscribeEvent
    public static void playerTick(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (!player.world.isRemote) {
                if (ResearchManager.syncList.remove(player.getName().getString()) != null) {
                    IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(player);
                    knowledge.sync((ServerPlayerEntity) player);
                }
            }
        }
    }

}
