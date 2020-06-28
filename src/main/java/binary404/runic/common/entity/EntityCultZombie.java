package binary404.runic.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class EntityCultZombie extends MonsterEntity {

    public EntityCultZombie(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCultZombie(World world) {
        this(ModEntities.CULT_ZOMBIE, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new LookAtGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(4, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 0.987F));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.987F, 64));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((double)0.23F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(40.D);
    }

    public float auraSize = 5F;

    @Override
    public void tick() {
        super.tick();
        List<LivingEntity> targets = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPosition()).grow(auraSize / 2));
        for (LivingEntity target : targets) {
            if (target instanceof EntityCultZombie)
                continue;
            target.attackEntityFrom(DamageSource.WITHER, 1F);
        }
        auraSize = this.getHealth() / 4F;
    }
}
