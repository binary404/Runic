package binary404.runic.common.blocks.fluid;

import binary404.runic.Runic;
import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.core.util.Utils;
import binary404.runic.common.items.ModItems;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryFluids {

    public static ForgeFlowingFluid.Properties SOLVENT_PROPERTIES;

    public static FluidSolvent.Flowing SOLVENT_FLOWING;
    public static FluidSolvent.Source SOLVENT_SOURCE;

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
        IForgeRegistry r = event.getRegistry();
        makeProperties();

        SOLVENT_SOURCE = registerFluid(new FluidSolvent.Source(SOLVENT_PROPERTIES), r);
        SOLVENT_FLOWING = registerFluid(new FluidSolvent.Flowing(SOLVENT_PROPERTIES), r);
    }

    private static void makeProperties() {
        SOLVENT_PROPERTIES = makeProperties(FluidSolvent.class, FluidSolvent::addAttributes, () -> SOLVENT_SOURCE, () -> SOLVENT_FLOWING).block(() -> ModBlocks.solvent).bucket(() -> ModItems.BUCKET_SOLVENT);
    }

    private static ForgeFlowingFluid.Properties makeProperties(Class<? extends ForgeFlowingFluid> fluidClass,
                                                               Function<FluidAttributes.Builder, FluidAttributes.Builder> postProcess,
                                                               Supplier<ForgeFlowingFluid> stillFluidSupplier,
                                                               Supplier<ForgeFlowingFluid> flowingFluidSupplier) {
        String name = Utils.fromClass(fluidClass, "Fluid").getPath();
        return new ForgeFlowingFluid.Properties(
                stillFluidSupplier,
                flowingFluidSupplier,
                postProcess.apply(builderFor(name)));
    }

    private static FluidAttributes.Builder builderFor(String fluidName) {
        ResourceLocation still = Runic.key("fluid/" + fluidName + "_still");
        ResourceLocation flowing = Runic.key("fluid/" + fluidName + "_flowing");
        return FluidAttributes.builder(still, flowing);
    }

    private static <T extends Fluid> T registerFluid(T fluid, IForgeRegistry r) {
        return registerFluid(fluid, Utils.fromClass(fluid, "Fluid", "Source"), r);
    }

    private static <T extends Fluid> T registerFluid(T fluid, ResourceLocation name, IForgeRegistry r) {
        fluid.setRegistryName(name);
        r.register(fluid);
        return fluid;
    }
}
