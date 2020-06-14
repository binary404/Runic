package binary404.runic.common.core.feature.power;

import net.minecraft.util.math.BlockPos;

public interface IPowerReciever {

    public void recievePower(int power);

    public BlockPos getPos();

    public boolean canRecievePower();

}
