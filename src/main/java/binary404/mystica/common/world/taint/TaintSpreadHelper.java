package binary404.mystica.common.world.taint;

import binary404.mystica.Mystica;
import binary404.mystica.api.MysticaMaterials;
import binary404.mystica.api.block.ITaintedBlock;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.blocks.world.taint.BlockTaintFiber;
import binary404.mystica.common.blocks.world.taint.BlockTaintLog;
import binary404.mystica.common.core.util.BlockUtils;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.IPlantable;

public class TaintSpreadHelper {

    public static float mod = 0.001F;

    //Spreads fibers only. If top is true then it will only spread on the surface
    public static void spreadFibers(World world, BlockPos pos, boolean top) {
        int xx = pos.getX() + world.rand.nextInt(3) - 1;
        int zz = pos.getZ() + world.rand.nextInt(3) - 1;
        int yy = pos.getY() + world.rand.nextInt(3) - 1;
        BlockPos t;
        if (top)
            t = new BlockPos(xx, world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, xx, zz), zz);
        else
            t = new BlockPos(xx, yy, zz);

        if (t.equals(pos)) {
            return;
        }

        BlockState bs = world.getBlockState(t);
        if (!isOnlyAdjacentToTaint(world, t) && BlockUtils.isAdjacentToSolidBlock(world, t) && (bs.getBlock() instanceof AirBlock || bs.getBlock() instanceof FlowerBlock || bs.getBlock() instanceof IPlantable || bs.getMaterial().isReplaceable()) && !(bs.getBlock() instanceof LeavesBlock)) {
            world.setBlockState(t, BlockTaintFiber.getBlockStateFor(world, t));
            world.addBlockEvent(t, ModBlocks.taint_fiber, 1, 0);
            world.func_230547_a_(t, ModBlocks.taint_fiber);
            if (world.rand.nextInt(5) == 0)
                world.getPendingBlockTicks().scheduleTick(t, ModBlocks.taint_fiber, 20);
            return;
        }
    }

    public static void spreadTaintAt(World world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Material material = bs.getMaterial();
        if (bs.getBlock() instanceof LeavesBlock) {
            Direction face = null;
            if (world.rand.nextFloat() < 0.6D && (face = BlockUtils.getFaceBlockTouching(world, pos, ModBlocks.taint_log)) != null) {
                //Feature
            } else {
                world.setBlockState(pos, BlockTaintFiber.getBlockStateFor(world, pos));
                WeaveHelper.drainFlux(world, pos, 0.01F);
            }
            return;
        }
        if (isHemmedByTaint(world, pos)) {
            if (BlockTags.LOGS.contains(bs.getBlock())) {
                world.setBlockState(pos, ModBlocks.taint_log.getDefaultState().with(BlockTaintLog.AXIS, world.getBlockState(pos).get(BlockTaintLog.AXIS)));
                return;
            }
            if (material == Material.SAND || material == Material.CLAY || material == Material.EARTH || material == Material.ORGANIC) {
                world.setBlockState(pos, ModBlocks.taint_soil.getDefaultState());
                return;
            }
            if (material == Material.ROCK) {
                world.setBlockState(pos, ModBlocks.taint_rock.getDefaultState());
                return;
            }
        }
    }

    public static void spreadTaint(World world, BlockPos pos) {
        int xx = pos.getX() + world.rand.nextInt(3) - 1;
        int yy = pos.getY() + world.rand.nextInt(3) - 1;
        int zz = pos.getZ() + world.rand.nextInt(3) - 1;

        BlockPos t = new BlockPos(xx, yy, zz);
        spreadTaintAt(world, t);
    }

    public static void startFibers(World world, BlockPos pos) {
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos t = pos.add(x, 0, z);
                BlockState bs = world.getBlockState(t);
                if (BlockUtils.isAdjacentToSolidBlock(world, t) || (bs.getBlock() instanceof AirBlock || bs.getBlock() instanceof FlowerBlock || bs.getBlock() instanceof IPlantable) && world.rand.nextInt(3) == 0) {
                    world.setBlockState(t, BlockTaintFiber.getBlockStateFor(world, t));
                    world.func_230547_a_(t, ModBlocks.taint_fiber);
                }
            }
        }
    }

    public static boolean isOnlyAdjacentToTaint(World world, BlockPos pos) {
        for (Direction dir : Direction.VALUES) {
            if (!world.isAirBlock(pos.offset(dir)) && world.getBlockState(pos.offset(dir)).getMaterial() != MysticaMaterials.MATERIAL_TAINT && world.getBlockState(pos.offset(dir)).isSolid())
                return false;
        }
        return true;
    }

    public static boolean canExist(World world, BlockPos pos) {
        return WeaveHelper.getFlux(world, pos) > 4;
    }

    public static boolean isHemmedByTaint(World world, BlockPos pos) {
        int c = 0;
        for (Direction dir : Direction.VALUES) {
            BlockState block = world.getBlockState(pos.offset(dir));
            if (block.getMaterial() == MysticaMaterials.MATERIAL_TAINT) {
                c++;
            } else if (world.isAirBlock(pos.offset(dir))) {
                c--;
            } else if (!block.getMaterial().isLiquid() && !block.isSolidSide(world, pos.offset(dir), dir.getOpposite())) {
                c--;
            }
        }
        return (c > 0);
    }

}
