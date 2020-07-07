package binary404.runic.common.core.util;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
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

}
