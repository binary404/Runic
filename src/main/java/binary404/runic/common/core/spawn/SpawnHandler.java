package binary404.runic.common.core.spawn;

import binary404.runic.Runic;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SpawnHandler {

    @SubscribeEvent
    public static void spawnMobs(TickEvent.WorldTickEvent event) {
        if (event.world instanceof ServerWorld) {
            CultSpawner.spawnCults((ServerWorld) event.world);
        }
    }

}
