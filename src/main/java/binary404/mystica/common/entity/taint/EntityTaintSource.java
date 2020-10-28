package binary404.mystica.common.entity.taint;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityTaintSource extends Entity {

    int lifespan = 3000;

    public EntityTaintSource(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.setMotion(0.0D, 0.0D, 0.0D);
    }

    public EntityTaintSource(EntityType<?> entityType, World worldIn, BlockPos pos, int lifespan) {
        this(entityType, worldIn);
        setPosition(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        this.prevPosX = pos.getX() + 0.5;
        this.prevPosX = pos.getY() + 0.5;
        this.prevPosZ = pos.getZ() + 0.5;
        this.lifespan = lifespan;
    }

    @Override
    protected void registerData() {
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("lifespan", this.lifespan);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.lifespan = compound.getInt("lifespan");
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }
}
