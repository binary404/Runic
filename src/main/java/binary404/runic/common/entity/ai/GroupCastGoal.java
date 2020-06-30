package binary404.runic.common.entity.ai;

import binary404.runic.common.core.util.ModSounds;
import binary404.runic.common.entity.EntityCultZombie;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.EnumSet;
import java.util.List;

public class GroupCastGoal extends Goal {

    private final EntityCultZombie monster;
    protected final Class<? extends EntityCultZombie> helperClass;
    List<EntityCultZombie> nearby;

    public GroupCastGoal(EntityCultZombie monster, Class<? extends EntityCultZombie> helperClass) {
        this.helperClass = helperClass;
        this.monster = monster;
        this.setMutexFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
    }

    @Override
    public boolean shouldExecute() {
        this.nearby = monster.world.getEntitiesWithinAABB(helperClass, new AxisAlignedBB(monster.getPosition()).grow(15.0D, 15.0D, 15.0D));
        if (this.monster.getRNG().nextFloat() >= 0.2) {
            return false;
        }
        if (this.nearby.size() <= 0) {
            return false;
        }
        return this.nearby.size() >= 3;
    }

    @Override
    public boolean shouldContinueExecuting() {
        this.nearby = monster.world.getEntitiesWithinAABB(helperClass, new AxisAlignedBB(monster.getPosition()).grow(15.0D, 15.0D, 15.0D));
        int count = this.nearby.size();
        for (LivingEntity helper : this.nearby) {
            if (!helper.isAlive()) {
                count--;
            }
        }
        if (count >= 3) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void startExecuting() {
        this.nearby = monster.world.getEntitiesWithinAABB(helperClass, new AxisAlignedBB(monster.getPosition()).grow(15.0D, 15.0D, 15.0D));
        int x = (int) this.monster.getPosX();
        int y = (int) this.monster.getPosY();
        int z = (int) this.monster.getPosZ();
        for (EntityCultZombie entity : this.nearby) {
            x += entity.getPosX();
            y += entity.getPosY();
            z += entity.getPosZ();
        }
        x = x / (nearby.size() + 1);
        y = y / (nearby.size() + 1);
        z = z / (nearby.size() + 1);
        BlockPos castPos = new BlockPos(x, y - 2, z);
        this.monster.castPos = castPos;
        for (EntityCultZombie entity : this.nearby) {
            entity.castPos = castPos;
            for (PrioritizedGoal entry : entity.goalSelector.goals) {
                if (entry.getGoal() instanceof GroupCastGoal) {
                    entry.startExecuting();
                } else {
                    entry.resetTask();
                }
            }
        }

        for (PrioritizedGoal entry : this.monster.goalSelector.goals) {
            if (entry.getGoal() instanceof GroupCastGoal) {
            } else {
                entry.resetTask();
            }
        }

    }

    @Override
    public void resetTask() {
        this.nearby.clear();
        this.monster.castPos = null;
    }

    @Override
    public void tick() {
        this.monster.heal(0.5F);
        if(this.monster.ticksExisted % 60 == 0 && this.monster.world.rand.nextBoolean()) {
            this.monster.playSound(ModSounds.chant, 10.F, 1.0F);
        }
        this.monster.getLookController().setLookPosition(this.monster.castPos.getX(), this.monster.castPos.getY(), this.monster.castPos.getZ());
    }
}
