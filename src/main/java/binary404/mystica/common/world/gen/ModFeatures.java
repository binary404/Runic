package binary404.mystica.common.world.gen;

import binary404.mystica.Mystica;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static final Feature<NoFeatureConfig> THREADS = new ThreadGenFeature();

    public static final ConfiguredFeature<?, ?> THREADS_CONF = THREADS.withConfiguration(new NoFeatureConfig()).withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event) {
        IForgeRegistry<Feature<?>> r = event.getRegistry();

        register(r, THREADS, "threads");

        Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, Mystica.key("threads"), THREADS_CONF);
    }

    @Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class BiomeHandler {

        @SubscribeEvent
        public static void onBiomeLoad(BiomeLoadingEvent event) {
            event.getGeneration().withFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, THREADS_CONF);
        }
    }

}
