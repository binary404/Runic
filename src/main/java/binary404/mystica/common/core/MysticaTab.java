package binary404.mystica.common.core;

import binary404.mystica.common.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MysticaTab extends ItemGroup {

    public static final MysticaTab INSTANCE = new MysticaTab();

    public MysticaTab() {
        super("mystica");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.guide);
    }

}
