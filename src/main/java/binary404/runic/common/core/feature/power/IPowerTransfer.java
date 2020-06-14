package binary404.runic.common.core.feature.power;

import net.minecraft.util.math.BlockPos;

public interface IPowerTransfer {

    public void transferPower(IPowerGenerator generator, IPowerReciever reciever);

}
