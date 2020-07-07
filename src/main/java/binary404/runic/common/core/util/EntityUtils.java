package binary404.runic.common.core.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityUtils {

    public static Vec3d posToHand(Entity e, Hand hand) {
        double px = e.getPosX();
        double py = (e.getBoundingBox()).minY + (e.getHeight() / 2.0F) + 0.25D;
        double pz = e.getPosZ();
        float m = (hand == Hand.MAIN_HAND) ? 0.0F : 180.0F;
        px += (-MathHelper.cos((e.rotationYaw + m) / 180.0F * 3.141593F) * 0.3F);
        pz += (-MathHelper.sin((e.rotationYaw + m) / 180.0F * 3.141593F) * 0.3F);
        Vec3d vec3d = e.getLook(1.0F);
        px += vec3d.x * 0.3D;
        py += vec3d.y * 0.3D;
        pz += vec3d.z * 0.3D;
        return new Vec3d(px, py, pz);
    }

}
