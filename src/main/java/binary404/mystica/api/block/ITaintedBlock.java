package binary404.mystica.api.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface ITaintedBlock {

    void die(World world, BlockPos pos, BlockState state);

}
