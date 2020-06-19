package binary404.runic.common.entity;

import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

public class EntitySpecialItem extends ItemEntity {

    public EntitySpecialItem(World world) {
        super(ModEntities.SPECIAL_ITEM, world);
    }

    public EntitySpecialItem(World worldIn, double x, double y, double z, ItemStack stack) {
        super(worldIn, x, y, z, stack);
    }

    public EntitySpecialItem(EntityType<? extends ItemEntity> p_i50217_1_, World p_i50217_2_) {
        super(p_i50217_1_, p_i50217_2_);
    }

    public EntitySpecialItem(World worldIn, double x, double y, double z) {
        this(ModEntities.SPECIAL_ITEM, worldIn);
        this.setPosition(x, y, z);
        this.rotationYaw = this.rand.nextFloat() * 360.0F;
        this.setMotion(this.rand.nextDouble() * 0.2D - 0.1D, 0.2D, this.rand.nextDouble() * 0.2D - 0.1D);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source == DamageSource.IN_FIRE)
            return true;
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean isBurning() {
        return false;
    }

    @Override
    protected void dealFireDamage(int amount) {

    }

    @Override
    public void tick() {
        if (this.world.getBlockState(this.getPosition()).getBlock() == Blocks.LAVA) {
            world.setBlockState(this.getPosition(), ModBlocks.solvent.getDefaultState());
            this.remove();
        }
        super.tick();
    }
}
