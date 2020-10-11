package binary404.mystica.common.core.capability;

import binary404.mystica.api.capability.IWeave;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WeaveProvider implements ICapabilitySerializable<INBT> {

    public static final ResourceLocation NAME = new ResourceLocation("mystica", "weave");

    @CapabilityInject(IWeave.class)
    public static final Capability<IWeave> WEAVE = null;

    private LazyOptional<IWeave> instance = LazyOptional.of(WEAVE::getDefaultInstance);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return cap == WEAVE ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return WEAVE.getStorage().writeNBT(WEAVE, instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional is empty")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        WEAVE.getStorage().readNBT(WEAVE, instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional is empty")), null, nbt);
    }
}
