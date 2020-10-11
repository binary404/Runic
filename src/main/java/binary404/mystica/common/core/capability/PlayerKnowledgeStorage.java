package binary404.mystica.common.core.capability;

import binary404.mystica.api.capability.IPlayerKnowledge;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class PlayerKnowledgeStorage implements Capability.IStorage<IPlayerKnowledge> {

    @Nullable
    @Override
    public INBT writeNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, Direction side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IPlayerKnowledge> capability, IPlayerKnowledge instance, Direction side, INBT nbt) {
        if (nbt instanceof CompoundNBT)
            instance.deserializeNBT((CompoundNBT) nbt);
    }

}
