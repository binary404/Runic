package binary404.runic.common.blocks.fluid;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.Rarity;
import net.minecraft.state.StateContainer;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public abstract class FluidSolvent extends ForgeFlowingFluid {

    private FluidSolvent(Properties properties) {
        super(properties);
    }

    public static FluidAttributes.Builder addAttributes(FluidAttributes.Builder builder) {
        return builder.rarity(Rarity.EPIC);
    }

    public static class Flowing extends FluidSolvent {

        public Flowing(Properties properties) {
            super(properties);
            setDefaultState(getStateContainer().getBaseState().with(LEVEL_1_8, 7));
        }

        @Override
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        @Override
        public int getLevel(IFluidState p_207192_1_) {
            return p_207192_1_.get(LEVEL_1_8);
        }

        @Override
        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    public static class Source extends FluidSolvent {

        public Source(Properties properties) {
            super(properties);
        }

        @Override
        public int getLevel(IFluidState p_207192_1_) {
            return 8;
        }

        @Override
        public boolean isSource(IFluidState state) {
            return true;
        }
    }

}
