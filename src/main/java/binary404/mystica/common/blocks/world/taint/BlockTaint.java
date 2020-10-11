package binary404.mystica.common.blocks.world.taint;

import binary404.mystica.api.MysticaMaterials;
import binary404.mystica.api.block.ITaintedBlock;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.world.taint.TaintSpreadHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class BlockTaint extends Block implements ITaintedBlock {

    public BlockTaint() {
        super(Properties.create(MysticaMaterials.MATERIAL_TAINT).hardnessAndResistance(10.0F, 100.0F).tickRandomly());
    }

    @Override
    public void die(World world, BlockPos pos, BlockState state) {
        if (state.getBlock() == ModBlocks.taint_rock) {
            world.setBlockState(pos, Blocks.STONE.getDefaultState());
        } else if (state.getBlock() == ModBlocks.taint_soil) {
            world.setBlockState(pos, Blocks.DIRT.getDefaultState());
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!TaintSpreadHelper.canExist(worldIn, pos)) {
            die(worldIn, pos, state);
        }
        TaintSpreadHelper.spreadTaint(worldIn, pos);
        for (Direction direction : Direction.VALUES)
            TaintSpreadHelper.spreadFibers(worldIn, pos.offset(direction), false);
    }
}
