package binary404.mystica.common.world.weave.thread_types;

import binary404.mystica.common.entity.weave.EntityWeaveThread;
import binary404.mystica.common.world.weave.ThreadType;
import binary404.mystica.common.world.weave.thread_types.ThreadTypeNormal;

public class ThreadTypeUnstable extends ThreadTypeNormal {

    public ThreadTypeUnstable(int id) {
        super(id, 0.4F, 0.4F, 0.5F);
    }

    @Override
    public void performPeriodicEvent(EntityWeaveThread entity) {
        super.performPeriodicEvent(entity);

        if (entity.world.rand.nextInt(33) == 0) {
            int type = entity.world.rand.nextInt(ThreadType.threadTypes.length - 1) + 1;
            entity.setThreadType(type);
        }
    }
}
