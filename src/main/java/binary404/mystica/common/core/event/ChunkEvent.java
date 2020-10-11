package binary404.mystica.common.core.event;

import binary404.mystica.Mystica;
import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IWeave;
import binary404.mystica.common.world.weave.WeaveHandlerThread;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkWatchEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ChunkEvent {

    @SubscribeEvent
    public static void chunkLoad(ChunkWatchEvent.Watch event) {
        ServerWorld world = event.getWorld();
        Chunk chunk = world.getChunk(event.getPos().x, event.getPos().z);
        IWeave weave = CapabilityHelper.getWeave(chunk);
        if (!weave.isGenerated()) {
            generateWeave(weave, world.rand);
        }
        WeaveHelper.addWeaveChunk(world.getDimensionKey(), chunk);
    }

    @SubscribeEvent
    public static void chunkUnload(ChunkWatchEvent.UnWatch event) {
        WeaveHelper.removeWeaveChunk(event.getWorld().getDimensionKey(), event.getPos().x, event.getPos().z);
    }

    @SubscribeEvent
    public static void worldLoad(WorldEvent.Load event) {
        if (!event.getWorld().isRemote()) {
            WeaveHelper.addWeaveWorld(((World) event.getWorld()).getDimensionKey());
        }
    }

    @SubscribeEvent
    public static void worldUnload(WorldEvent.Unload event) {
        if (event.getWorld().isRemote()) {
            return;
        }
        WeaveHelper.removeWeaveWorld(((World) event.getWorld()).getDimensionKey());
    }

    public static void generateWeave(IWeave weave, Random rand) {
        float life = 1.0F;

        life /= 5.0F;

        float noise = (float) (1.0D + rand.nextGaussian() * 0.2);
        short base = (short) (int) (life * 500.0F * noise);
        base = (short) MathHelper.clamp(base, 0, 500);
        weave.setWeave(base);
        weave.setIsGenerated(true);
    }

}
