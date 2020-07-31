package binary404.runic.common.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.ai.goal.RangedAttackGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class EntityBeholder extends MonsterEntity implements IRangedAttackMob, IFlyingAnimal {

    public EntityBeholder(EntityType<? extends MonsterEntity> p_i48578_1_, World p_i48578_2_) {
        super(p_i48578_1_, p_i48578_2_);
        this.moveController = new FlyingMovementController(this, 20, true);
    }

    public EntityBeholder(World world) {
        this(ModEntities.BEHOLDER, world);
    }


    public float getBlockPathWeight(BlockPos pos, IWorldReader worldIn) {
        return worldIn.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    protected PathNavigator createNavigator(World worldIn) {
        FlyingPathNavigator flyingpathnavigator = new FlyingPathNavigator(this, worldIn) {
            public boolean canEntityStandOnPos(BlockPos pos) {
                return !this.world.getBlockState(pos.down()).isAir();
            }

            public void tick() {
                super.tick();
            }
        };
        flyingpathnavigator.setCanOpenDoors(false);
        flyingpathnavigator.setCanSwim(false);
        flyingpathnavigator.setCanEnterDoors(true);
        return flyingpathnavigator;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new WanderGoal());
        this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.0f, 60, 20));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<PlayerEntity>(this, PlayerEntity.class, false));
    }

    @Override
    public void attackEntityWithRangedAttack(LivingEntity livingEntity, float v) {
        binary404.fx_lib.fx.ParticleDispatcher.arcFX(this.getPosX(), this.getPosY(), this.getPosZ(), livingEntity.getPosX(), livingEntity.getPosY(), livingEntity.getPosZ(), 1.0F, 1.0F, 1.0F);
        livingEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this), 6.0F);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    /**
     * Returns true if this entity should move as if it were on a ladder (either because it's actually on a ladder, or
     * for AI reasons)
     */
    public boolean isOnLadder() {
        return false;
    }

    class WanderGoal extends Goal {
        WanderGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return EntityBeholder.this.navigator.noPath();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            return EntityBeholder.this.navigator.func_226337_n_();
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            Vector3d vec3d = this.getRandomLocation();
            if (vec3d != null) {
                EntityBeholder.this.navigator.setPath(EntityBeholder.this.navigator.getPathToPos(new BlockPos(vec3d), 1), 1.0D);
            }

        }

        @Nullable
        private Vector3d getRandomLocation() {
            net.minecraft.util.math.vector.Vector3d vec3d;
            vec3d = EntityBeholder.this.getLook(0.0F);
            Vector3d vec3d2 = RandomPositionGenerator.findAirTarget(EntityBeholder.this, 8, 7, vec3d, ((float) Math.PI / 2F), 2, 1);
            return vec3d2 != null ? vec3d2 : RandomPositionGenerator.findGroundTarget(EntityBeholder.this, 8, 4, -2, vec3d, (double) ((float) Math.PI / 2F));
        }
    }

}
