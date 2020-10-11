package binary404.mystica.common.entity.weave;

import binary404.fx_lib.fx.ParticleDispatcher;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.core.network.fx.PacketArcFX;
import binary404.mystica.common.entity.ModEntities;
import binary404.mystica.common.world.weave.ThreadType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;

public class EntityWeaveThread extends Entity {

    int checkDelay;
    private int tickCounter = -1;

    ArrayList<EntityWeaveThread> neighbors;

    private static final DataParameter<Float> size = EntityDataManager.createKey(EntityWeaveThread.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> type = EntityDataManager.createKey(EntityWeaveThread.class, DataSerializers.VARINT);

    public EntityWeaveThread(World world) {
        this(ModEntities.THREAD, world);
    }

    public EntityWeaveThread(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);

        this.checkDelay = -1;
        this.neighbors = null;
        this.noClip = true;
    }

    @Override
    protected void registerData() {
        this.dataManager.register(size, 0F);
        this.dataManager.register(type, 0);
    }

    private void checkAdjacentNodes() {
        if (this.neighbors == null || this.checkDelay < this.ticksExisted) {
            this.neighbors = (ArrayList<EntityWeaveThread>) this.world.getEntitiesWithinAABB(EntityWeaveThread.class, new AxisAlignedBB(this.getPosition()).grow(32.0D));
            this.checkDelay = this.ticksExisted + 750;
        }
    }

    @Override
    public void tick() {
        if (getThreadSize() == 0) {
            randomizeThread();
        }
        if (this.tickCounter < 0)
            this.tickCounter = this.rand.nextInt(200);
        this.world.getProfiler().startSection("entityBaseTick");
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        ThreadType.threadTypes[getThreadType()].performTickEvent(this);
        if (this.tickCounter++ > 200) {
            this.tickCounter = 0;
            ThreadType.threadTypes[getThreadType()].performPeriodicEvent(this);
        }
        checkAdjacentNodes();
        if (this.ticksExisted % 20 == 0)
            for (EntityWeaveThread thread : this.neighbors) {
                PacketHandler.sendToNearby(world, this, new PacketArcFX(this.getPosX() + 0.25, this.getPosY() + 0.25, this.getPosZ() + 0.25, thread.getPosX() + 0.25, thread.getPosY() + 0.25, thread.getPosZ() + 0.25, 1.0F, 0.2F, 0.5F, true));
            }

        if (this.getMotion().x != 0.0D || this.getMotion().y != 0.0D || this.getMotion().z != 0.0D) {
            this.getMotion().mul(0.8D, 0.8D, 0.8D);
            super.moveForced(this.getMotion().x, this.getMotion().y, this.getMotion().z);
        }
        this.world.getProfiler().endSection();
    }

    public void randomizeThread() {
        setThreadSize(1 + this.rand.nextInt(4));

        if (this.rand.nextInt(5) == 0 && ThreadType.threadTypes.length > 1) {
            setThreadType(1 + this.rand.nextInt(ThreadType.threadTypes.length - 1));
        } else {
            setThreadType(0);
        }
    }

    public float getThreadSize() {
        return this.dataManager.get(size);
    }

    public void setThreadSize(float sizeToSet) {
        this.dataManager.set(size, sizeToSet);
    }

    public int getThreadType() {
        return this.dataManager.get(type);
    }

    public void setThreadType(int param1) {
        this.dataManager.set(type, param1);
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        this.setThreadSize(compound.getFloat("thread_size"));
        this.setThreadType(compound.getInt("thread_type"));
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putFloat("thread_size", getThreadSize());
        compound.putInt("thread_type", getThreadType());
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean isImmuneToExplosions() {
        return true;
    }

    @Override
    public boolean isImmuneToFire() {
        return true;
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    @Override
    public void addVelocity(double x, double y, double z) {
    }

    @Override
    public void move(MoverType typeIn, Vector3d pos) {
    }

    @Override
    protected boolean shouldSetPosAfterLoading() {
        return false;
    }

    @Override
    public boolean canRenderOnFire() {
        return false;
    }
}
