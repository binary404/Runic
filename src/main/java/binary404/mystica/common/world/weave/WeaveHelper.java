package binary404.mystica.common.world.weave;

import binary404.mystica.Mystica;
import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IWeave;
import binary404.mystica.common.core.capability.Weave;
import binary404.mystica.common.core.capability.WeaveProvider;
import binary404.mystica.common.core.util.PosXY;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.concurrent.ConcurrentHashMap;

public class WeaveHelper {

    static ConcurrentHashMap<RegistryKey<World>, WeaveWorld> weaves = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<RegistryKey<World>, BlockPos> fluxTrigger = new ConcurrentHashMap<>();

    public static WeaveWorld getWeaveWorld(RegistryKey<World> dim) {
        return weaves.get(dim);
    }

    public static Chunk getWeaveChunk(RegistryKey<World> dim, int x, int y) {
        if (weaves.containsKey(dim)) {
            return weaves.get(dim).getWeaveChunkAt(x, y);
        }
        addWeaveWorld(dim);
        if (weaves.containsKey(dim))
            return weaves.get(dim).getWeaveChunkAt(x, y);
        return null;
    }

    public static void addRechargeTicket(RegistryKey<World> dim, int x, int y, int recharge) {
        getWeaveWorld(dim).addRechargeTicket(new PosXY(x, y), recharge);
    }

    public static void addWeaveWorld(RegistryKey<World> dim) {
        if (!weaves.containsKey(dim)) {
            weaves.put(dim, new WeaveWorld(dim));
            Mystica.LOGGER.info("Creating weave for world " + dim);
        }
    }

    public static void removeWeaveWorld(RegistryKey<World> dim) {
        weaves.remove(dim);
        Mystica.LOGGER.info("Removing weave for world " + dim);
    }

    public static void addWeaveChunk(RegistryKey<World> dim, Chunk chunk) {
        WeaveWorld ww = weaves.get(dim);
        if (ww == null) {
            ww = new WeaveWorld(dim);
        }

        ww.getWeaveChunks().put(new PosXY(chunk.getPos().x, chunk.getPos().z), chunk);

        weaves.put(dim, ww);
    }

    public static void removeWeaveChunk(RegistryKey<World> dim, int x, int z) {
        WeaveWorld ww = weaves.get(dim);
        if (ww != null) {
            ww.getWeaveChunks().remove(new PosXY(x, z));
        }
    }

    public static IWeave getWeaveAt(World world, BlockPos pos) {
        return CapabilityHelper.getWeave(world.getChunkAt(pos));
    }

    public static float getTotalWeave(World world, BlockPos pos) {
        return getWeaveAt(world, pos).getTotalWeave();
    }

    public static float getFluxSaturation(World world, BlockPos pos) {
        return getWeaveAt(world, pos).getFluxSaturation();
    }

    public static float getMystic(World world, BlockPos pos) {
        return getWeaveAt(world, pos).getMystic();
    }

    public static float getFlux(World world, BlockPos pos) {
        return getWeaveAt(world, pos).getFlux();
    }

    public static int getBase(World world, BlockPos pos) {
        return getWeaveAt(world, pos).getBase();
    }

    public static void addMystic(World world, BlockPos pos, float amount) {
        getWeaveAt(world, pos).addMystic(amount);
    }

    public static void addFlux(World world, BlockPos pos, float amount) {
        getWeaveAt(world, pos).addFlux(amount);
    }

    public static float drainMystic(World world, BlockPos pos, float amount) {
        return getWeaveAt(world, pos).drainMystic(amount);
    }

    public static float drainFlux(World world, BlockPos pos, float amount) {
        return getWeaveAt(world, pos).drainFlux(amount);
    }

}
