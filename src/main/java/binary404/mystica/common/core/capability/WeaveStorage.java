package binary404.mystica.common.core.capability;

import binary404.mystica.api.capability.IWeave;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class WeaveStorage implements Capability.IStorage<IWeave> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IWeave> capability, IWeave instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IWeave> capability, IWeave instance, Direction side, INBT nbt) {
        if(nbt instanceof CompoundNBT)
            instance.deserializeNBT((CompoundNBT) nbt);
    }
}
