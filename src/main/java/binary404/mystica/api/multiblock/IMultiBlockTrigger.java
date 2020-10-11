package binary404.mystica.api.multiblock;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface IMultiBlockTrigger {

    public static final ArrayList<IMultiBlockTrigger> triggers = new ArrayList<IMultiBlockTrigger>();

    Placement getValidFace(final World p0, final PlayerEntity p1, final BlockPos p2, final Direction p3);

    void execute(final World p0, final PlayerEntity p1, final BlockPos p2, final Placement p3, final Direction p4);

    default List<BlockPos> sparkle(final World world, final PlayerEntity player, final BlockPos pos, final Placement placement) {
        return Arrays.asList(pos);
    }

    static void registerDustTrigger(final IMultiBlockTrigger trigger) {
        IMultiBlockTrigger.triggers.add(trigger);
    }

    public static class Placement {
        public int xOffset;
        public int yOffset;
        public int zOffset;
        public Direction facing;

        public Placement(final int xOffset, final int yOffset, final int zOffset, final Direction facing) {
            this.xOffset = xOffset;
            this.yOffset = yOffset;
            this.zOffset = zOffset;
            this.facing = facing;
        }
    }

}
