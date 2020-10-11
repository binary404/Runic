package binary404.mystica.common.core.spawn;

import binary404.mystica.Mystica;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnHandler {

    @SubscribeEvent
    public static void spawnMobs(TickEvent.WorldTickEvent event) {
        if (event.world instanceof ServerWorld) {
            CultSpawner.spawnCults((ServerWorld) event.world);
        }
    }

}
