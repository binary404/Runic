package binary404.mystica.common.core.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockUtils {

    public static Direction[] HORIZONTALS = new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST};

    public static boolean isBlockExposed(final World world, final BlockPos pos) {
        for (final Direction face : Direction.values()) {
            if (!world.getBlockState(pos.offset(face)).isOpaqueCube(world, pos)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAdjacentToSolidBlock(World world, BlockPos pos) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.offset(face)).isSolidSide(world, pos, face.getOpposite()))
                return true;
        }
        return false;
    }

    public static Direction getFaceBlockTouching(IBlockReader world, BlockPos pos, Block bs) {
        for (Direction face : Direction.values()) {
            if (world.getBlockState(pos.offset(face)).getBlock() == bs) return face;
        }
        return null;
    }

}
