package binary404.mystica.common.entity;

import binary404.mystica.common.entity.hostile.EntityCultZombie;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityCultOrb extends ThrowableEntity {

    protected EntityCultOrb(EntityType<? extends ThrowableEntity> p_i48540_1_, World p_i48540_2_) {
        super(p_i48540_1_, p_i48540_2_);
    }

    public EntityCultOrb(LivingEntity p_i48542_2_, World p_i48542_3_) {
        super(ModEntities.CULT_ORB, p_i48542_2_, p_i48542_3_);
    }

    public EntityCultOrb(World world) {
        this(ModEntities.CULT_ORB, world);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        List<LivingEntity> list = this.world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB(this.getPosition()).grow(2.0D));
        for (LivingEntity target : list) {
            if (target instanceof EntityCultZombie) {
                break;
            }
            target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.func_234616_v_()), 2.0F);
            if (world.rand.nextInt(10) == 0)
                target.addPotionEffect(new EffectInstance(Effects.WITHER, 100, 0));
        }
    }

    @Override
    protected void registerData() {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
