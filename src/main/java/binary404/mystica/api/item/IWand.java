package binary404.mystica.api.item;

import binary404.mystica.api.aspect.Aspect;
import binary404.mystica.api.aspect.AspectList;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface IWand {

    int getMaxMystic(ItemStack stack);

    int getMystic(ItemStack stack);

    boolean consumeMystic(ItemStack stack, PlayerEntity player, int amount, boolean doIt);

    int addMystic(ItemStack stack, int amount);

    ItemStack getFocusStack(ItemStack stack);

    float getConsumptionRate(ItemStack stack);

    void setFocus(ItemStack stack, ItemStack focus);

    WandRod getRod(ItemStack stack);

    void setRod(ItemStack stack, WandRod rod);

    WandCap getCap(ItemStack stack);

    void setCap(ItemStack stack, WandCap cap);


}
