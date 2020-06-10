package binary404.runic.common.core;

import binary404.runic.Runic;
import binary404.runic.common.items.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class RunicTab extends ItemGroup {

    public static final RunicTab INSTANCE = new RunicTab();

    public RunicTab() {
        super("runic");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.guide);
    }

}
