package binary404.mystica.common.core.util;

import binary404.mystica.api.item.IReaveler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.system.CallbackI;

public class EntityUtils {

    public static Vector3d posToHand(Entity e, Hand hand) {
        double px = e.getPosX();
        double py = (e.getBoundingBox()).minY + (e.getHeight() / 2.0F) + 0.25D;
        double pz = e.getPosZ();
        float m = (hand == Hand.MAIN_HAND) ? 0.0F : 180.0F;
        px += (-MathHelper.cos((e.rotationYaw + m) / 180.0F * 3.141593F) * 0.3F);
        pz += (-MathHelper.sin((e.rotationYaw + m) / 180.0F * 3.141593F) * 0.3F);
        Vector3d vec3d = e.getLook(1.0F);
        px += vec3d.x * 0.3D;
        py += vec3d.y * 0.3D;
        pz += vec3d.z * 0.3D;
        return new Vector3d(px, py, pz);
    }

    public static boolean hasRevealer(Entity e) {
        if (!(e instanceof PlayerEntity))
            return false;
        PlayerEntity player = (PlayerEntity) e;
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof IReaveler)
            return ((IReaveler) player.getHeldItemMainhand().getItem()).showNodes(player.getHeldItemMainhand(), player);

        for (int a = 0; a < 4; a++) {
            if (((ItemStack) player.inventory.armorInventory.get(a)).getItem() instanceof IReaveler)
                return ((IReaveler) player.inventory.armorInventory.get(a).getItem()).showNodes(player.inventory.armorInventory.get(a), player);
        }
        return false;
    }

    public static double getThreadViewDistance(Entity e) {
        if (!(e instanceof PlayerEntity))
            return 0.0D;
        PlayerEntity player = (PlayerEntity) e;
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() instanceof IReaveler)
            return ((IReaveler) player.getHeldItemMainhand().getItem()).viewDistance();

        for (int a = 0; a < 4; a++) {
            if (((ItemStack) player.inventory.armorInventory.get(a)).getItem() instanceof IReaveler)
                return ((IReaveler) player.inventory.armorInventory.get(a).getItem()).viewDistance();
        }
        return 0.0D;
    }

}
