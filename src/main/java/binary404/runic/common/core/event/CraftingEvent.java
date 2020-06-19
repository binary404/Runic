package binary404.runic.common.core.event;

import binary404.runic.Runic;
import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.config.ResearchManager;
import binary404.runic.common.entity.EntitySpecialItem;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CraftingEvent {

    @SubscribeEvent
    public static void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && !event.getPlayer().world.isRemote) {
            int stackHash = ResearchManager.createItemStackHash(event.getCrafting().copy());
            if (ResearchManager.craftingReferences.contains(stackHash)) {
                ResearchManager.completeResearch(event.getPlayer(), "[#]" + stackHash);
            } else {
                stackHash = ResearchManager.createItemStackHash(new ItemStack(event.getCrafting().getItem(), event.getCrafting().getCount()));
                if (ResearchManager.craftingReferences.contains(stackHash)) {
                    ResearchManager.completeResearch(event.getPlayer(), "[#]" + stackHash);
                }
            }
        }
    }

    @SubscribeEvent
    public static void entityAdded(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }
        Entity entity = event.getEntity();
        if (entity instanceof ItemEntity && !(entity instanceof EntitySpecialItem)) {
            ItemEntity entityItem = (ItemEntity) entity;
            ItemStack stack = entityItem.getItem();
            if(stack != null && stack.getItem() == Items.LAPIS_LAZULI) {
                EntitySpecialItem newEntity = new EntitySpecialItem(event.getWorld(), entityItem.getPosX(), entityItem.getPosY(), entityItem.getPosZ(), stack);
                newEntity.setMotion(entityItem.getMotion().x, entityItem.getMotion().y, entityItem.getMotion().z);
                newEntity.setDefaultPickupDelay();
                if (newEntity != null) {
                    event.getEntity().remove();
                    event.setCanceled(true);
                    event.getWorld().addEntity(newEntity);
                }
            }
        }
    }

}
