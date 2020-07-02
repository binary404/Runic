package binary404.runic.common.tile.world;

import binary404.runic.common.tile.ModTiles;
import binary404.runic.common.tile.TileMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
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

    public int ticks;

    public TileMobCrystal() {
        super(ModTiles.MOB_CRYSTAL);
    }

    @Override
    public void tick() {
        this.ticks++;
        List<MonsterEntity> mobs = world.getEntitiesWithinAABB(MonsterEntity.class, new AxisAlignedBB(this.pos).grow(5.0D));
        if (mobs.isEmpty()) {
            mobsNearby = false;
        } else {
            mobsNearby = true;
        }
    }

}
