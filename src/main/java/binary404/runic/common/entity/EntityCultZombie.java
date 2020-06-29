package binary404.runic.common.entity;

import binary404.runic.client.FXHelper;
import binary404.runic.common.entity.ai.GroupCastGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityCultZombie extends MonsterEntity {

    public EntityCultZombie(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCultZombie(World world) {
        this(ModEntities.CULT_ZOMBIE, world);
    }

    public int castingTicks = 0;
    public boolean canCast = true;
    public BlockPos castPos;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.987F));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.987F, 64));
        this.goalSelector.addGoal(1, new GroupCastGoal(this, EntityCultZombie.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double) 0.23F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.D);
    }

    public float auraSize = 5F;

    private int aura = 5;

    @Override
    public void tick() {
        super.tick();

        if (this.castPos != null) {
            Vec3d current = new Vec3d(this.getPosition().getX() + this.world.rand.nextGaussian() * 0.1, this.getPosition().getY() + this.world.rand.nextGaussian() * 0.1, this.getPosition().getZ() + this.world.rand.nextGaussian() * 0.1);
            Vec3d end = new Vec3d(this.castPos.getX(), this.castPos.getY(), this.castPos.getZ());
            Vec3d motion = end.subtract(current).normalize().mul(0.1, 0.1, 0.1);
            FXHelper.wisp(this.getPosX() + 0.5, this.getPosY() + 1, this.getPosZ() + 0.5, motion.x, motion.y, motion.z, 0.6F, 0.6F, 0.6F, 0.08F, 200);
        }

        List<LivingEntity> targets = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPosition()).grow(auraSize / 2));
        for (LivingEntity target : targets) {
            if (target instanceof EntityCultZombie)
                continue;
            target.attackEntityFrom(DamageSource.WITHER, 1F);
        }
        if (this.ticksExisted % 5 == 0) {
            aura = (int) this.getHealth() / 4;
            if (this.auraSize > aura) {
                this.auraSize--;
            } else if (this.auraSize < aura) {
                this.auraSize++;
            }
        }
    }
}
