package binary404.mystica.common.blocks.world.taint;

import binary404.mystica.api.MysticaMaterials;
import binary404.mystica.api.block.ITaintedBlock;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.world.taint.TaintSpreadHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockTaintFiber extends Block implements ITaintedBlock {

    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST = BooleanProperty.create("west");
    public static final BooleanProperty UP = BooleanProperty.create("up");
    public static final BooleanProperty DOWN = BooleanProperty.create("down");
    public static final BooleanProperty GROWTH1 = BooleanProperty.create("growth1");
    public static final BooleanProperty GROWTH2 = BooleanProperty.create("growth2");
    public static final BooleanProperty GROWTH3 = BooleanProperty.create("growth3");
    public static final BooleanProperty GROWTH4 = BooleanProperty.create("growth4");

    protected static final VoxelShape DOWN_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.1D, 16.0D);
    protected static final VoxelShape UP_SHAPE = Block.makeCuboidShape(0.0D, 16.0D, 0.0D, 16.0D, 15.9D, 16.0D);

    public BlockTaintFiber() {
        super(Properties.create(MysticaMaterials.MATERIAL_TAINT).notSolid().tickRandomly().hardnessAndResistance(1.0F));

        this.setDefaultState(this.getStateContainer().getBaseState().with(GROWTH1, false).with(NORTH, false)
                .with(EAST, false).with(SOUTH, false).with(WEST, false).with(UP, false)
                .with(GROWTH2, false).with(GROWTH3, false).with(GROWTH4, false));
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!TaintSpreadHelper.canExist(worldIn, pos)) {
            die(worldIn, pos, state);
            return;
        }
        worldIn.getPendingBlockTicks().scheduleTick(pos, this, 10);
        for (Direction direction : Direction.BY_HORIZONTAL_INDEX) {
            TaintSpreadHelper.spreadFibers(worldIn, pos.offset(direction), false);
        }
        if (!state.get(GROWTH1) && !state.get(GROWTH2) && !state.get(GROWTH3) && !state.get(GROWTH4)) {
            if (TaintSpreadHelper.isOnlyAdjacentToTaint(worldIn, pos)) {
                die(worldIn, pos, state);
            }
        }
        for (Direction dir : Direction.VALUES) {
            BlockPos t = pos.offset(dir);
            TaintSpreadHelper.spreadTaintAt(worldIn, t);
        }
    }

    @Override
    public void die(World world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState());
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        if (state.get(DOWN))
            return DOWN_SHAPE;
        if (state.get(UP))
            return UP_SHAPE;
        return VoxelShapes.empty();
    }

    @Override
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        worldIn.setBlockState(pos, getBlockStateFor(worldIn, pos));
    }

    public static BlockState getBlockStateFor(IWorld world, BlockPos pos) {
        Boolean[] connections = {false, false, false, false, false, false};
        int a = 0;
        for (Direction direction : Direction.VALUES) {
            if (world.getBlockState(pos.offset(direction)).isSolid()) {
                connections[a] = true;
            }
            a++;
        }

        int growth = 0;

        Random rand = new Random(pos.toLong());

        int q = rand.nextInt(40);
        if (world.getBlockState(pos.offset(Direction.DOWN)).isSolid()) {
            if (q < 6) {
                growth = 1;
            } else if (q == 7 || q == 8) {
                growth = 2;
            } else if (q == 9) {
                growth = 3;
            }
        }
        if (world.getBlockState(pos.offset(Direction.UP)).isSolid() && q > 37) {
            growth = 4;
        }
        return ModBlocks.taint_fiber.getDefaultState().with(DOWN, connections[0])
                .with(UP, connections[1])
                .with(NORTH, connections[2])
                .with(SOUTH, connections[3])
                .with(WEST, connections[4])
                .with(EAST, connections[5])
                .with(GROWTH1, growth == 1)
                .with(GROWTH2, growth == 2)
                .with(GROWTH3, growth == 3)
                .with(GROWTH4, growth == 4);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        return getBlockStateFor(worldIn, pos);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos pos, BlockPos facingPos) {
        return getBlockStateFor(worldIn, pos);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getBlock() != ModBlocks.taint_fiber;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, SOUTH, EAST, WEST, UP, DOWN, GROWTH1, GROWTH2, GROWTH3, GROWTH4);
    }
}
