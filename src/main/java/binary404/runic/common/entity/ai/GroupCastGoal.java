package binary404.runic.common.entity.ai;

import binary404.runic.client.FXHelper;
import binary404.runic.client.libs.Vector3f;
import binary404.runic.common.entity.EntityCultZombie;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class GroupCastGoal extends Goal {

    private final EntityCultZombie monster;
    protected final Class<? extends EntityCultZombie> helperClass;
    List<EntityCultZombie> nearby;

    public GroupCastGoal(EntityCultZombie monster, Class<? extends EntityCultZombie> helperClass) {
        this.helperClass = helperClass;
        this.monster = monster;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
        this.nearby = monster.world.getEntitiesWithinAABB(helperClass, new AxisAlignedBB(monster.getPosition()).grow(15.0D, 15.0D, 15.0D));
    }

    @Override
    public boolean shouldExecute() {
        if (this.monster.getRNG().nextFloat() >= 0.2) {
            return false;
        }
        if (this.nearby.size() <= 0) {
            return false;
        }
        if (!monster.canCast) {
            return false;
        }
        return this.nearby.size() >= 3;
    }

    @Override
    public boolean shouldContinueExecuting() {
        int count = this.nearby.size();
        for (LivingEntity helper : this.nearby) {
            if (!helper.isAlive()) {
                count--;
            }
        }
        if (count >= 3 && monster.canCast) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void startExecuting() {
        int count = 0;
        int x = (int) this.monster.getPosX();
        int y = (int) this.monster.getPosY();
        int z = (int) this.monster.getPosZ();
        for (EntityCultZombie entity : this.nearby) {
            x += entity.getPosX();
            y += entity.getPosY();
            z += entity.getPosZ();
            count++;
        }
        x /= count;
        y /= count;
        z /= count;
        BlockPos castPos = new BlockPos(x, y, z);
        for (EntityCultZombie entity : this.nearby) {
            entity.castPos = castPos;
        }

    }

    @Override
    public void resetTask() {
        this.nearby.clear();
    }

    @Override
    public void tick() {
        this.monster.castingTicks++;
        this.monster.getLookController().setLookPosition(this.monster.castPos.getX(), this.monster.castPos.getY(), this.monster.castPos.getZ());
    }
}
