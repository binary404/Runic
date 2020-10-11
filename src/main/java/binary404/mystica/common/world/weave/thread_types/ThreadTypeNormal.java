package binary404.mystica.common.world.weave.thread_types;

import binary404.mystica.common.entity.weave.EntityWeaveThread;
import binary404.mystica.common.world.weave.ThreadType;
import binary404.mystica.common.world.weave.WeaveHelper;
import net.minecraft.world.chunk.Chunk;

public class ThreadTypeNormal extends ThreadType {

    public ThreadTypeNormal(int id, float r, float g, float b) {
        super(id, r, g, b);
    }

    @Override
    public void performTickEvent(EntityWeaveThread entity) {

    }

    @Override
    public void performPeriodicEvent(EntityWeaveThread entity) {
        if (entity.world.isRemote)
            return;
        Chunk chunk = entity.world.getChunkAt(entity.getPosition());
        WeaveHelper.addRechargeTicket(entity.world.getDimensionKey(), chunk.getPos().x, chunk.getPos().z, calculateStrength(entity));
    }

    @Override
    public int calculateStrength(EntityWeaveThread entity) {
        int m = entity.world.getMoonPhase();
        float b = 1.0F + (Math.abs(m - 4) - 2) / 5.0F;
        return (int) Math.max(1.0D, Math.sqrt((entity.getThreadSize() / 3.0F)) * b);
    }
}
