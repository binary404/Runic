package binary404.mystica.common.items;

import binary404.mystica.api.item.IReaveler;
import binary404.mystica.api.item.IWeaveViewer;
import binary404.mystica.common.core.MysticaTab;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemMysticometer extends Item implements IReaveler, IWeaveViewer {

    public ItemMysticometer() {
        super(new Properties().maxStackSize(1).group(MysticaTab.INSTANCE));
    }


    @Override
    public boolean showNodes(ItemStack stack, LivingEntity entity) {
        return true;
    }

    @Override
    public double viewDistance() {
        return 200;
    }
}
