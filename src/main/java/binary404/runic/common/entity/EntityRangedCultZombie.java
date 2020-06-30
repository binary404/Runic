package binary404.runic.common.entity;

import binary404.runic.common.entity.ai.GroupCastGoal;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class EntityRangedCultZombie extends EntityCultZombie implements IRangedAttackMob {

    public EntityRangedCultZombie(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityRangedCultZombie(World world) {
        super(ModEntities.RANGED_CULT_ZOMBIE, world);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.987F));
        this.goalSelector.addGoal(3, new RangedAttackGoal(this, 1.0F, 60, 64));
        this.goalSelector.addGoal(3, new GroupCastGoal(this, EntityCultZombie.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity livingEntity, float v) {
        EntityCultOrb entity = new EntityCultOrb(this, world);
        double x = livingEntity.getPosX() - this.getPosX();
        double y = livingEntity.getPosY() - this.getPosY();
        double z = livingEntity.getPosZ() - this.getPosZ();
        entity.shoot(x * 0.2F, y * 0.2F, z * 0.2f, 1.6F, 0);
        this.world.addEntity(entity);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
    }

    @Override
    public void tick() {
        super.tick();
        this.auraSize = 0F;
    }
}
