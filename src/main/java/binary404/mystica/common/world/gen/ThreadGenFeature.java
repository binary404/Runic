package binary404.mystica.common.world.gen;

import binary404.mystica.common.entity.weave.EntityWeaveThread;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

public class ThreadGenFeature extends Feature<NoFeatureConfig> {

    public ThreadGenFeature() {
        super(NoFeatureConfig.field_236558_a_);
    }

    @Override
    public boolean func_241855_a(ISeedReader world, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (rand.nextInt(10) == 0) {
            int x = pos.getX() + rand.nextInt(16);
            int z = pos.getZ() + rand.nextInt(16);
            int y = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z) + 3 + rand.nextInt(3);
            EntityWeaveThread thread = new EntityWeaveThread(world.getWorld());
            thread.setPosition(x + 0.5, y + 0.5, z + 0.5);
            world.func_242417_l(thread);
            return true;
        }
        return false;
    }
}
