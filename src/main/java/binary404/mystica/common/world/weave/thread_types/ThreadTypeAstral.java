package binary404.mystica.common.world.weave.thread_types;

import binary404.mystica.common.entity.weave.EntityWeaveThread;

public class ThreadTypeAstral extends ThreadTypeNormal {

    public ThreadTypeAstral(int id) {
        super(id, 0.1F, 0.3F, 0.7F);
    }

    @Override
    public int calculateStrength(EntityWeaveThread entity) {
        int m = entity.world.getMoonPhase();
        float b = 1.0F + (Math.abs(m - 4) - 2) / 5.0F;
        b -= (entity.getBrightness() - 0.5F) / 3.0F;
        return (int) Math.max(1.0D, Math.sqrt((entity.getThreadSize() / 3.0F)) * b);
    }
}
