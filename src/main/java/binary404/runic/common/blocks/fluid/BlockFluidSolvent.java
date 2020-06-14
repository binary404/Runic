package binary404.runic.common.blocks.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;

import java.util.function.Supplier;

public class BlockFluidSolvent extends FlowingFluidBlock {

    public BlockFluidSolvent(Supplier<? extends FlowingFluid> fluid) {
        super(fluid, Block.Properties.create(Material.WATER).lightValue(10).hardnessAndResistance(1000.0F).noDrops().doesNotBlockMovement());
    }
}
