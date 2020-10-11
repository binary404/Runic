package binary404.mystica.common.core.capability;

import binary404.mystica.api.capability.IWeave;
import net.minecraft.nbt.CompoundNBT;

public class Weave implements IWeave {

    private float mystic;
    private float flux;
    private short base;
    public boolean isGenerated;

    public Weave() {

    }

    @Override
    public float getTotalWeave() {
        return mystic + flux;
    }

    @Override
    public float getFluxSaturation() {
        return flux / base;
    }

    @Override
    public float getMystic() {
        return mystic;
    }

    @Override
    public float getFlux() {
        return flux;
    }

    @Override
    public short getBase() {
        return base;
    }

    @Override
    public void addMystic(float amount) {
        if (amount < 0.0F)
            return;
        this.mystic = Math.max(0.0F, this.mystic + amount);
    }

    @Override
    public void addFlux(float amount) {
        if (amount < 0.0F)
            return;
        this.flux = Math.max(0.0F, this.flux + amount);
    }

    @Override
    public float drainMystic(float amount) {
        if (amount > this.mystic)
            amount = this.mystic;
        this.mystic = Math.max(0.0F, this.mystic - amount);

        return amount;
    }

    @Override
    public float drainFlux(float amount) {
        if (amount > this.flux)
            amount = this.flux;
        this.flux = Math.max(0.0F, this.flux - amount);

        return amount;
    }

    @Override
    public void setWeave(short base) {
        this.base = base;
        this.mystic = base;
    }

    @Override
    public void setMystic(float mystic) {
        this.mystic = mystic;
    }

    @Override
    public void setFlux(float flux) {
        this.flux = flux;
    }

    @Override
    public void setBase(short base) {
        this.base = base;
    }

    @Override
    public boolean isGenerated() {
        return isGenerated;
    }

    @Override
    public void setIsGenerated(boolean isGenerated) {
        this.isGenerated = isGenerated;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putFloat("mystic", this.mystic);
        nbt.putFloat("flux", this.flux);
        nbt.putShort("base", this.base);
        nbt.putBoolean("generated", this.isGenerated);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.mystic = nbt.getFloat("mystic");
        this.flux = nbt.getFloat("flux");
        this.base = nbt.getShort("base");
        this.isGenerated = nbt.getBoolean("generated");
    }
}
