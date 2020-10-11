package binary404.mystica.common.world.weave;

import binary404.mystica.Mystica;
import binary404.mystica.api.capability.CapabilityHelper;
import binary404.mystica.api.capability.IWeave;
import binary404.mystica.common.core.capability.Weave;
import binary404.mystica.common.core.event.ServerEvents;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.lwjgl.system.CallbackI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WeaveHandlerThread implements Runnable {

    public RegistryKey<World> dim;
    private final long INTERVAL = 1000L;
    private boolean stop;
    Random rand;
    private float phaseMystic;
    private float phaseFlux;
    private float phaseMax;
    private long lastWorldTime;
    private float[] phaseTable;
    private float[] maxTable;

    public WeaveHandlerThread(RegistryKey<World> dim) {
        this.stop = false;
        this.rand = new Random(System.currentTimeMillis());

        this.phaseMystic = 0.0F;
        this.phaseFlux = 0.0F;
        this.phaseMax = 0.0F;
        this.lastWorldTime = 0L;

        this.phaseTable = new float[]{0.25F, 0.15F, 0.1F, 0.05F, 0.0F, 0.05F, 0.1F, 0.15F};
        this.maxTable = new float[]{0.15F, 0.05F, 0.0F, -0.05F, -0.15F, -0.05F, 0.0F, 0.05F};

        this.dim = dim;
    }

    @Override
    public void run() {
        Mystica.LOGGER.info("Starting weave thread for world " + this.dim);

        while (!this.stop) {
            if (WeaveHelper.weaves.isEmpty()) {
                Mystica.LOGGER.warn("No weave worlds found");

                break;
            }
            long startTime = System.currentTimeMillis();

            WeaveWorld world = WeaveHelper.getWeaveWorld(this.dim);

            if (world != null) {
                ServerWorld server = ServerLifecycleHooks.getCurrentServer().getWorld(dim);
                if (this.lastWorldTime != server.getGameTime()) {
                    this.lastWorldTime = server.getGameTime();
                    if (server != null) {
                        this.phaseMystic = this.phaseTable[server.getMoonPhase()];
                        this.phaseMax = 1.0F + this.maxTable[server.getMoonPhase()];
                        this.phaseFlux = 0.25F - this.phaseMystic;
                    }
                    for (Chunk weaveChunk : world.weaveChunks.values()) {
                        if (weaveChunk.loaded)
                            processChunk(world, weaveChunk);
                        else
                            WeaveHelper.removeWeaveChunk(this.dim, weaveChunk.getPos().x, weaveChunk.getPos().z);
                    }
                }
            } else {
                stop();
            }

            long executionTime = System.currentTimeMillis() - startTime;

            try {
                if (executionTime > 1000L) {
                    Mystica.LOGGER.warn("Weave Handling took " + (executionTime - 1000l) + " ms longer than normal");
                }
                Thread.sleep(Math.max(1L, 1000L - executionTime));
            } catch (InterruptedException e) {

            }
        }

        Mystica.LOGGER.info("Stopping weave thread for dimension " + this.dim);

        try {
            ServerEvents.weaveThreads.remove(this.dim);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processChunk(WeaveWorld world, Chunk chunk) {
        if (chunk != null) {
            List<Integer> directions = Arrays.asList(new Integer[]{Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3)});
            Collections.shuffle(directions, this.rand);
            IWeave weave = CapabilityHelper.getWeave(chunk);
            int x = chunk.getPos().x;
            int y = chunk.getPos().z;
            float base = weave.getBase() * this.phaseMax;

            boolean dirty = false;

            float currentMystic = weave.getMystic();
            float currentFlux = weave.getFlux();

            IWeave neighbourMysticChunk = null;
            IWeave neighbourFluxChunk = null;
            float lowestMystic = Float.MAX_VALUE;
            float lowestFlux = Float.MAX_VALUE;

            for (Integer a : directions) {
                Direction dir = Direction.byHorizontalIndex(a);
                IWeave n = CapabilityHelper.getWeave(world.getWeaveChunkAt(x + dir.getXOffset(), y + dir.getZOffset()));
                if (n != null) {
                    if ((neighbourMysticChunk == null || lowestMystic > n.getMystic()) && n.getMystic() + n.getFlux() < n.getBase() * this.phaseMax) {
                        neighbourMysticChunk = n;
                        lowestMystic = n.getMystic();
                    }
                    if (neighbourFluxChunk == null || lowestFlux > n.getFlux()) {
                        neighbourFluxChunk = n;
                        lowestFlux = n.getFlux();
                    }
                }
            }

            if (neighbourMysticChunk != null && lowestMystic < currentMystic && (lowestMystic / currentMystic) < 0.75D) {
                float inc = Math.min(currentMystic - lowestMystic, 1.0F);
                currentMystic -= inc;
                neighbourMysticChunk.setMystic(lowestMystic + inc);
                dirty = true;
            }


            if (neighbourFluxChunk != null && currentFlux >
                    Math.max(5.0F, weave.getBase() / 10.0F) && lowestFlux < currentFlux / 1.75D) {

                float inc = Math.min(currentFlux - lowestFlux, 1.0F);
                currentFlux -= inc;
                neighbourFluxChunk.setFlux(lowestFlux + inc);
                dirty = true;
            }

        /*
        if (currentMystic + currentFlux < base) {
            float inc = Math.min(base - currentMystic + currentFlux, this.phaseMystic);
            currentMystic += inc;
            dirty = true;
        } */

            if (currentMystic + currentFlux < base) {
                try {
                    currentMystic += world.getRechargeTicket(chunk.getPos().x, chunk.getPos().z);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dirty = true;
            }

            if (currentMystic > base * 1.25D && this.rand.nextFloat() < 0.1D) {
                currentFlux += this.phaseFlux;
                currentMystic -= this.phaseFlux;
                dirty = true;
            } else if (currentMystic <= base * 0.1D && currentMystic >= currentFlux && this.rand.nextFloat() < 0.1D) {
                currentFlux += this.phaseFlux;
                dirty = true;
            }

            if (dirty) {
                weave.setMystic(currentMystic);
                weave.setFlux(currentFlux);
            }

            if (currentFlux > base * 0.75D && this.rand.nextFloat() < currentFlux / 500.0F / 10.0F) {
                WeaveHelper.fluxTrigger.put(world.dim, new BlockPos(x * 16, 0, y * 16));
            }
        }
    }

    public void stop() {
        this.stop = true;
    }

}
