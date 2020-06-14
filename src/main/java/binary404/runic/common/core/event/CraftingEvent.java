package binary404.runic.common.core.event;

import binary404.runic.Runic;
import binary404.runic.common.config.ResearchManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
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

}
