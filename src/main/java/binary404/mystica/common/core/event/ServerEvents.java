package binary404.mystica.common.core.event;

import binary404.mystica.Mystica;
import binary404.mystica.common.world.taint.TaintEvents;
import binary404.mystica.common.world.taint.TaintSpreadHelper;
import binary404.mystica.common.world.weave.WeaveHandlerThread;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents {

    public static ConcurrentHashMap<RegistryKey<World>, WeaveHandlerThread> weaveThreads = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        RegistryKey<World> dimension = event.world.getDimensionKey();

        if (event.phase == TickEvent.Phase.START) {
            if (!weaveThreads.containsKey(dimension) && WeaveHelper.getWeaveWorld(dimension) != null) {
                WeaveHandlerThread at = new WeaveHandlerThread(dimension);
                Thread thread = new Thread(at);
                thread.start();
                weaveThreads.put(dimension, at);
            }
        } else {
            if (WeaveHelper.fluxTrigger.containsKey(dimension)) {
                TaintSpreadHelper.startFibers(event.world, WeaveHelper.fluxTrigger.get(dimension));
                TaintEvents.taintTriggerEvent(event.world, WeaveHelper.fluxTrigger.get(dimension));
            }
        }
    }

}
