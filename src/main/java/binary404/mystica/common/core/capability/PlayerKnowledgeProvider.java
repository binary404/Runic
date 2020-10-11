package binary404.mystica.common.core.capability;

import binary404.mystica.api.capability.IPlayerKnowledge;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

public class PlayerKnowledgeProvider implements ICapabilitySerializable<INBT> {

    public static final ResourceLocation NAME = new ResourceLocation("mystica", "research");

    @CapabilityInject(IPlayerKnowledge.class)
    public static final Capability<IPlayerKnowledge> KNOWLEDGE = null;

    private LazyOptional<IPlayerKnowledge> instance = LazyOptional.of(KNOWLEDGE::getDefaultInstance);

    public boolean hasCapability(Capability<?> capability, Direction direction) {
        return capability == KNOWLEDGE;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        return cap == KNOWLEDGE ? instance.cast() : LazyOptional.empty();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return getCapability(cap, null);
    }

    @Override
    public INBT serializeNBT() {
        return KNOWLEDGE.getStorage().writeNBT(KNOWLEDGE, instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional is empty")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        KNOWLEDGE.getStorage().readNBT(KNOWLEDGE, instance.orElseThrow(() -> new IllegalArgumentException("Lazy Optional is empty")), null, nbt);
    }

}
