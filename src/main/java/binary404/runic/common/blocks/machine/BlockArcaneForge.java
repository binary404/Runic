package binary404.runic.common.blocks.machine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

public class BlockArcaneForge extends Block {

    public BlockArcaneForge(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }


}
