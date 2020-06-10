package binary404.runic.api.capability;

import binary404.runic.api.research.ResearchCategory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.Set;

public interface IPlayerKnowledge extends INBTSerializable<CompoundNBT> {

    public void clear();

    public EnumResearchStatus getResearchStatus(@Nonnull String var1);

    public boolean isResearchComplete(String var1);

    public boolean isResearchKnown(String var1);

    public int getResearchStage(@Nonnull String var1);

    public boolean addResearch(@Nonnull String var1);

    public boolean setResearchStage(@Nonnull String var1, int var2);

    public boolean removeResearch(@Nonnull String var1);

    @Nonnull
    public Set<String> getResearchList();

    public boolean setResearchFlag(@Nonnull String var1, @Nonnull EnumResearchFlag var2);

    public boolean clearResearchFlag(@Nonnull String var1, @Nonnull EnumResearchFlag var2);

    public boolean hasResearchFlag(@Nonnull String var1, @Nonnull EnumResearchFlag var2);

    public void sync(ServerPlayerEntity var1);

    public static enum EnumResearchStatus {
        UNKNOWN,
        COMPLETE,
        IN_PROGRESS;
    }

    public static enum EnumResearchFlag {
        PAGE,
        RESEARCH,
        POPUP;
    }

}
