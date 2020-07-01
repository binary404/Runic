package binary404.runic.common.tile.world;

import binary404.runic.common.tile.ModTiles;
import binary404.runic.common.tile.TileMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nonnull;
import java.util.List;

public class TileMobCrystal extends TileMod implements ITickableTileEntity {

    public boolean mobsNearby;
    EntityType mobType;

    public int ticks;

    public TileMobCrystal() {
        super(ModTiles.MOB_CRYSTAL);
        mobType = EntityType.PIG;
    }

    @Override
    public void tick() {
        this.ticks++;
        Entity entity = mobType.create(world);
        Class type = entity.getClass();
        List mobs = world.getEntitiesWithinAABB(type, new AxisAlignedBB(this.pos).grow(5.0D));
        if (mobs.isEmpty()) {
            mobsNearby = false;
        } else {
            mobsNearby = true;
        }
    }

    public void setMobType(EntityType type) {
        this.mobType = type;
    }

    @Nonnull
    @Override
    public CompoundNBT write(CompoundNBT par1nbtTagCompound) {
        par1nbtTagCompound.putString("mob_id", this.mobType.getRegistryName().toString());
        return super.write(par1nbtTagCompound);
    }

    @Override
    public void read(CompoundNBT par1nbtTagCompound) {
        super.read(par1nbtTagCompound);
        this.mobType = Registry.ENTITY_TYPE.getOrDefault(new ResourceLocation(par1nbtTagCompound.getString("mob_id")));
    }
}
