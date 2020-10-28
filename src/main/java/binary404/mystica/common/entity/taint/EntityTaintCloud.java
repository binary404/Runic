package binary404.mystica.common.entity.taint;

import binary404.mystica.client.fx.FXHelper;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.core.network.fx.PacketArcFX;
import binary404.mystica.common.world.taint.TaintSpreadHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;

public class EntityTaintCloud extends EntityTaintSource {

    public EntityTaintCloud(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.lifespan = 3000;
    }

    public EntityTaintCloud(EntityType<?> entityType, World worldIn, BlockPos pos, int lifespan) {
        super(entityType, worldIn, pos, lifespan);
    }

    @Override
    public void tick() {
        super.tick();
        addCloudParticles();
        if (this.rand.nextInt(20) == 0) {
            createTaint();
        }
        if (this.lifespan-- <= 0) {
            this.setDead();
        }
    }

    private void addCloudParticles() {
        BlockPos pos = new BlockPos(this.getPosX(), this.getPosY(), this.getPosZ());

        for (int a = 0; a <= 4; a++) {
            BlockPos bp = pos.add(this.rand.nextInt(2) - this.rand.nextInt(2), this.rand.nextInt(1) - this.rand.nextInt(1), this.rand.nextInt(2) - this.rand.nextInt(2));
            FXHelper.drawTaintCloudParticles(bp.getX() + rand.nextFloat(), bp.getY() + rand.nextFloat(), bp.getZ() + rand.nextFloat());
        }
    }

    private void createTaint() {
        BlockPos bp = this.getPosition().add(rand.nextInt(16) - rand.nextInt(16), 0, rand.nextInt(16) - rand.nextInt(16));
        bp = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, bp);
        PacketHandler.sendToNearby(world, bp, new PacketArcFX(this.getPosX(), this.getPosY(), this.getPosZ(), bp.getX() + 0.5, bp.getY(), bp.getZ() + 0.5, 1.0F, 0.5F, 1.0F, false));
        TaintSpreadHelper.startFibers(world, bp);
    }


}
