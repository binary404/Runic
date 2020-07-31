package binary404.runic.common.core.event;

import binary404.runic.Runic;
import binary404.runic.api.multiblock.MultiBlockTrigger;
import binary404.runic.common.core.network.PacketHandler;
import binary404.runic.common.core.util.BlockUtils;
import binary404.runic.common.core.util.InventoryUtils;
import com.sun.org.apache.xpath.internal.operations.Mult;
import javafx.geometry.Side;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.NetworkRegistry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MultiblockEvents {

    static HashMap<DimensionType, Integer> serverTicks;
    private static HashMap<DimensionType, LinkedBlockingQueue<RunnableEntry>> serverRunList;
    public static HashMap<DimensionType, LinkedBlockingQueue<VirtualSwapper>> swapList;
    public static final Predicate<SwapperPredicate> DEFAULT_PREDICATE;

    @SubscribeEvent
    public static void worldTick(TickEvent.WorldTickEvent event) {
        if (event.side == LogicalSide.CLIENT) {
            return;
        }
        final DimensionType dim = event.world.func_230315_m_();
        if (!MultiblockEvents.serverTicks.containsKey(dim)) {
            MultiblockEvents.serverTicks.put(dim, 0);
        }
        LinkedBlockingQueue<RunnableEntry> rlist = MultiblockEvents.serverRunList.get(dim);
        if (rlist == null) {
            MultiblockEvents.serverRunList.put(dim, rlist = new LinkedBlockingQueue<RunnableEntry>());
        } else if (!rlist.isEmpty()) {
            final LinkedBlockingQueue<RunnableEntry> temp = new LinkedBlockingQueue<RunnableEntry>();
            while (!rlist.isEmpty()) {
                final RunnableEntry current = rlist.poll();
                if (current != null) {
                    if (current.delay > 0) {
                        final RunnableEntry runnableEntry = current;
                        --runnableEntry.delay;
                        temp.offer(current);
                    } else {
                        try {
                            current.runnable.run();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
            while (!temp.isEmpty()) {
                rlist.offer(temp.poll());
            }
        }
        final int ticks = MultiblockEvents.serverTicks.get(dim);
        tickBlockSwap(event.world);
        MultiblockEvents.serverTicks.put(dim, ticks + 1);
    }

    private static void tickBlockSwap(final World world) {
        final DimensionType dim = world.func_230315_m_();
        final LinkedBlockingQueue<VirtualSwapper> queue = MultiblockEvents.swapList.get(dim);
        if (queue != null) {
            while (!queue.isEmpty()) {
                VirtualSwapper vs = queue.poll();
                if (vs != null) {
                    if (vs.target != null) {
                        world.setBlockState(vs.pos, vs.target);
                    }
                }
            }
        }
    }

    public static void addSwapper(final World world, final BlockPos pos, final Object source, BlockState target, final boolean consumeTarget, final int life, final PlayerEntity player, final boolean fx, final boolean fancy, final int color, final boolean pickup, final boolean silk, final int fortune, final Predicate<SwapperPredicate> allowSwap) {
        DimensionType dim = world.func_230315_m_();
        LinkedBlockingQueue<VirtualSwapper> queue = MultiblockEvents.swapList.get(dim);
        if (queue == null) {
            MultiblockEvents.swapList.put(dim, new LinkedBlockingQueue<VirtualSwapper>());
            queue = MultiblockEvents.swapList.get(dim);
        }
        queue.offer(new VirtualSwapper(pos, source, target, consumeTarget, life, player, fx, fancy, color, pickup, silk, fortune, allowSwap));
        MultiblockEvents.swapList.put(dim, queue);
    }

    public static void addRunnableServer(final World world, final Runnable runnable, final int delay) {
        if (world.isRemote) {
            return;
        }
        LinkedBlockingQueue<RunnableEntry> rlist = MultiblockEvents.serverRunList.get(world.func_230315_m_());
        if (rlist == null) {
            MultiblockEvents.serverRunList.put(world.func_230315_m_(), rlist = new LinkedBlockingQueue<RunnableEntry>());
        }
        rlist.add(new RunnableEntry(runnable, delay));
    }

    static {
        MultiblockEvents.serverTicks = new HashMap<DimensionType, Integer>();
        MultiblockEvents.serverRunList = new HashMap<DimensionType, LinkedBlockingQueue<RunnableEntry>>();
        MultiblockEvents.swapList = new HashMap<DimensionType, LinkedBlockingQueue<VirtualSwapper>>();
        DEFAULT_PREDICATE = (Predicate) new Predicate<SwapperPredicate>() {
            public boolean test(@Nullable final SwapperPredicate pred) {
                return true;
            }
        };
    }

    public static class RunnableEntry {
        Runnable runnable;
        int delay;

        public RunnableEntry(final Runnable runnable, final int delay) {
            this.runnable = runnable;
            this.delay = delay;
        }
    }

    public static class SwapperPredicate {
        public World world;
        public PlayerEntity player;
        public BlockPos pos;

        public SwapperPredicate(final World world, final PlayerEntity player, final BlockPos pos) {
            this.world = world;
            this.player = player;
            this.pos = pos;
        }
    }

    public static class VirtualSwapper {
        int color;
        boolean fancy;
        Predicate<SwapperPredicate> allowSwap;
        int lifespan;
        BlockPos pos;
        Object source;
        BlockState target;
        PlayerEntity player;
        boolean fx;
        boolean silk;
        boolean pickup;
        boolean consumeTarget;
        int fortune;

        VirtualSwapper(final BlockPos pos, final Object source, BlockState t, final boolean consumeTarget, final int life, final PlayerEntity p, final boolean fx, final boolean fancy, final int color, final boolean pickup, final boolean silk, final int fortune, final Predicate<SwapperPredicate> allowSwap) {
            this.lifespan = 0;
            this.player = null;
            this.pos = pos;
            this.source = source;
            this.target = t;
            this.lifespan = life;
            this.player = p;
            this.consumeTarget = consumeTarget;
            this.fx = fx;
            this.fancy = fancy;
            this.allowSwap = allowSwap;
            this.silk = silk;
            this.fortune = fortune;
            this.pickup = pickup;
            this.color = color;
        }
    }
}
