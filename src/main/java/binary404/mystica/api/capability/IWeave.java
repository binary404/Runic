package binary404.mystica.api.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface IWeave extends INBTSerializable<CompoundNBT> {

    public float getTotalWeave();

    public float getFluxSaturation();

    public float getMystic();

    public float getFlux();

    public short getBase();

    public void addMystic(float amount);

    public void addFlux(float amount);

    public float drainMystic(float amount);

    public float drainFlux(float amount);

    public void setWeave(short base);

    public void setMystic(float mystic);

    public void setFlux(float flux);

    public void setBase(short base);

    public boolean isGenerated();

    public void setIsGenerated(boolean isGenerated);
}
