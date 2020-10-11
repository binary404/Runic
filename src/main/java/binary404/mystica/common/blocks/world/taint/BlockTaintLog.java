package binary404.mystica.common.blocks.world.taint;

import binary404.mystica.api.MysticaMaterials;
import binary404.mystica.api.block.ITaintedBlock;
import binary404.mystica.common.world.taint.TaintSpreadHelper;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.block.*;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class BlockTaintLog extends RotatedPillarBlock implements ITaintedBlock {

    public BlockTaintLog() {
        super(Properties.create(MysticaMaterials.MATERIAL_TAINT).hardnessAndResistance(3.0F, 100.0F).harvestLevel(0).harvestTool(ToolType.AXE).tickRandomly());
    }

    @Override
    public int getFlammability(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 4;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, IBlockReader world, BlockPos pos, Direction face) {
        return 4;
    }

    @Override
    public void die(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
        WeaveHelper.addFlux(world, pos, 2.0F);
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if(!TaintSpreadHelper.canExist(worldIn, pos)) {
            die(worldIn, pos, state);
        }
        TaintSpreadHelper.spreadTaint(worldIn, pos);
    }



}
