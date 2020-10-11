package binary404.mystica.common.tile.world;

import binary404.mystica.common.tile.ModTiles;
import binary404.mystica.common.tile.TileMod;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.AxisAlignedBB;

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
