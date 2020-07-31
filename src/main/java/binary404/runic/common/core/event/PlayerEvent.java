package binary404.runic.common.core.event;

import binary404.runic.Runic;
import binary404.runic.api.capability.CapabilityHelper;
import binary404.runic.api.capability.IPlayerKnowledge;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvent {

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.ticksExisted % 20 == 0) {
            World world = event.player.world;
            if (world.getBlockState(event.player.func_233580_cy_()).getBlock() == Blocks.LAVA) {
                IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(event.player);
                if (knowledge.isResearchKnown("FIRST_STEPS@2") && !knowledge.isResearchKnown("FIRST_STEPS@3")) {
                    knowledge.addResearch("f_lava_swim");
                    if (event.player instanceof ServerPlayerEntity)
                        knowledge.sync((ServerPlayerEntity) event.player);
                    if (world.isRemote)
                        event.player.sendStatusMessage(new TranslationTextComponent(TextFormatting.DARK_RED + I18n.format("f_lava_swim.msg")), true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void playerWakeUpEvent(PlayerWakeUpEvent event) {
        IPlayerKnowledge knowledge = CapabilityHelper.getKnowledge(event.getPlayer());
        if (knowledge.isResearchKnown("FIRST_STEPS@3") && !knowledge.isResearchKnown("epiphany_solvent")) {
            knowledge.addResearch("epiphany_solvent");
            if(event.getPlayer() instanceof ServerPlayerEntity) {
                knowledge.sync((ServerPlayerEntity) event.getPlayer());
            }
            if(event.getPlayer().world.isRemote)
                event.getPlayer().sendStatusMessage(new TranslationTextComponent(TextFormatting.DARK_RED + I18n.format("epiphany_solvent.msg")), true);
        }
    }

}
