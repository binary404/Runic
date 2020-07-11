package binary404.runic.common.blocks.machine;

import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockArcaneForgeFurnace extends Block {

    private static final VoxelShape BASE = Block.makeCuboidShape(0, 0, 0, 16, 7, 16);
    private static final VoxelShape MIDDLE = Block.makeCuboidShape(2, 7, 2, 12, 9, 12);
    private static final VoxelShape TOP = Block.makeCuboidShape(1, 16, 1, 14, 11, 14);
    private static final VoxelShape SHAPE = VoxelShapes.or(VoxelShapes.or(BASE, MIDDLE), TOP);

    public BlockArcaneForgeFurnace(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        for (int a = -2; a <= 2; ++a) {
            for (int b = -1; b <= 1; ++b) {
                for (int c = -2; c <= 2; ++c) {
                    BlockState bs = worldIn.getBlockState(pos.add(a, b, c));
                    if (bs.getBlock() == ModBlocks.arcane_anvil) {
                        BlockArcaneForge.destroyForge(worldIn, pos.add(a, b, c), bs, pos);
                    }
                }
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
