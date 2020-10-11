package binary404.mystica.common.world.weave.thread_types;

import binary404.mystica.common.entity.weave.EntityWeaveThread;
import binary404.mystica.common.world.weave.WeaveHelper;

public class ThreadTypePure extends ThreadTypeNormal {

    public ThreadTypePure(int id) {
        super(id, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void performPeriodicEvent(EntityWeaveThread entity) {
        super.performPeriodicEvent(entity);
        if (WeaveHelper.drainFlux(entity.world, entity.getPosition(), this.calculateStrength(entity)) == this.calculateStrength(entity) && entity.world.rand.nextFloat() < 0.025F) {
            entity.setThreadSize(entity.getThreadSize() - 0.25F);
        }
    }
}
