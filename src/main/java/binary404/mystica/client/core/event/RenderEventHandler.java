package binary404.mystica.client.core.event;

import binary404.mystica.Mystica;
import binary404.mystica.client.core.handler.HudHandler;
import binary404.mystica.client.fx.FXHelper;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderEventHandler {

    public static HudHandler hudHandler = new HudHandler();

    @SubscribeEvent
    public static void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.getRenderViewEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) Minecraft.getInstance().getRenderViewEntity();
                hudHandler.renderHuds(minecraft, event.renderTickTime, player, System.currentTimeMillis());
            }
        }
    }

}
