package binary404.mystica.common.world.weave;

import binary404.mystica.common.entity.weave.EntityWeaveThread;
import binary404.mystica.common.world.weave.thread_types.*;

public abstract class ThreadType {

    public int id;
    public float r, g, b;

    public ThreadType(int id, float r, float g, float b) {
        this.id = id;
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static ThreadType[] threadTypes = new ThreadType[7];

    static {
        threadTypes[0] = new ThreadTypeNormal(0, 0.96F, 0.62F, 0.89F);
        threadTypes[1] = new ThreadTypeUnstable(1); // Dark
        threadTypes[2] = new ThreadTypeUnstable(2); // hungry
        threadTypes[3] = new ThreadTypePure(3);
        threadTypes[4] = new ThreadTypeTaint(4);
        threadTypes[5] = new ThreadTypeUnstable(5);
        threadTypes[6] = new ThreadTypeAstral(6);
    }

    abstract public void performTickEvent(EntityWeaveThread entity);

    abstract public void performPeriodicEvent(EntityWeaveThread entity);

    abstract public int calculateStrength(EntityWeaveThread entity);

}
