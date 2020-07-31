package binary404.runic.common.entity;

import binary404.runic.common.core.network.PacketHandler;
import binary404.runic.common.core.network.fx.PacketCultFX;
import binary404.runic.common.entity.ai.GroupCastGoal;
import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import java.util.List;

public class EntityCultZombie extends MonsterEntity {

    public EntityCultZombie(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityCultZombie(World world) {
        this(ModEntities.CULT_ZOMBIE, world);
    }

    public BlockPos castPos;

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new LookAtGoal(this, LivingEntity.class, 10.0F));
        this.goalSelector.addGoal(3, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomWalkingGoal(this, 0.987F));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 0.987F, 64));
        this.goalSelector.addGoal(3, new GroupCastGoal(this, EntityCultZombie.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, false));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    public float auraSize = 5F;

    private int aura = 5;

    @Override
    public void tick() {
        super.tick();

        if (this.castPos != null) {
            Vector3d current = new Vector3d(this.func_233580_cy_().getX() + this.world.rand.nextGaussian() * 0.1, this.func_233580_cy_().getY() + this.world.rand.nextGaussian() * 0.1, this.func_233580_cy_().getZ() + this.world.rand.nextGaussian() * 0.1);
            Vector3d end = new Vector3d(this.castPos.getX(), this.castPos.getY(), this.castPos.getZ());
            net.minecraft.util.math.vector.Vector3d motion = end.subtract(current).normalize().mul(0.1, 0.1, 0.1);
            PacketHandler.sendToNearby(world, this, new PacketCultFX(this.getPosX() + 0.2, this.getPosY() + 1.5, this.getPosZ() + 0.1, motion.x, motion.y, motion.z));
        }

        List<LivingEntity> targets = world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.func_233580_cy_()).grow(auraSize / 2));
        for (LivingEntity target : targets) {
            if (target instanceof EntityCultZombie)
                continue;
            target.attackEntityFrom(DamageSource.causeMobDamage(this), 1F);
        }
        if (this.ticksExisted % 5 == 0) {
            aura = (int) this.getHealth() / 8;
            if (this.auraSize > aura) {
                this.auraSize--;
            } else if (this.auraSize < aura) {
                this.auraSize++;
            }
        }
    }
}
