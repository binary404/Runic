package binary404.runic.common.blocks.fluid;

import binary404.runic.common.core.RunicTab;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class ItemSolventBucket extends BucketItem {

    public ItemSolventBucket(Supplier<? extends Fluid> fluid) {
        super(fluid, new Item.Properties().containerItem(Items.BUCKET).maxStackSize(1).group(RunicTab.INSTANCE));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new FluidBucketWrapper(stack);
    }

}
