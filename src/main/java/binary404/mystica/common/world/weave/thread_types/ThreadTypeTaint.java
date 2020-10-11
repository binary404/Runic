package binary404.mystica.common.world.weave.thread_types;

import binary404.mystica.common.entity.weave.EntityWeaveThread;
import binary404.mystica.common.world.weave.WeaveHelper;
import binary404.mystica.common.world.weave.thread_types.ThreadTypeNormal;

public class ThreadTypeTaint extends ThreadTypeNormal {

    public ThreadTypeTaint(int id) {
        super(id, 0.49F, 0.05F, 0.4F);
    }

    @Override
    public void performPeriodicEvent(EntityWeaveThread entity) {
        int strength = this.calculateStrength(entity);
        WeaveHelper.addFlux(entity.world, entity.getPosition(), strength);
    }
}
