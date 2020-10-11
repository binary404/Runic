package binary404.mystica.common.core.event;

import binary404.mystica.Mystica;
import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IPlayerKnowledge;
import binary404.mystica.api.capability.IWeave;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.core.network.PacketUpdateMystic;
import binary404.mystica.common.core.network.fx.PacketMysticaFX;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEvent {

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.ticksExisted % 40 == 0) {
            World world = event.player.world;
            PlayerEntity playerEntity = event.player;
            IWeave weave = WeaveHelper.getWeaveAt(playerEntity.world, playerEntity.getPosition());
            PacketHandler.sendToNearby(world, playerEntity, new PacketUpdateMystic(weave.getMystic(), weave.getFlux(), weave.getBase()));
        }
    }
}
