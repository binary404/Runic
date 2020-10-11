package binary404.mystica.api.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface IReaveler {

    boolean showNodes(ItemStack stack, LivingEntity entity);

    double viewDistance();

}
