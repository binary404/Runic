package binary404.runic.common.world.dungeon;

import binary404.runic.Runic;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.placement.IPlacementConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModStructures {

    public static Structure<NoFeatureConfig> dungeon;

    @SubscribeEvent
    public static void setup(RegistryEvent.Register<Feature<?>> event) {
        dungeon = new DungeonStructure();
        event.getRegistry().register(dungeon);
        Runic.LOGGER.error("REGISTERING DUNGEON");
    }

    public static void init() {
        for(Biome b : ForgeRegistries.BIOMES.getValues()) {
            ConfiguredFeature configured = dungeon.withConfiguration(NoFeatureConfig.NO_FEATURE_CONFIG);
            ConfiguredFeature decorated = configured.withPlacement(Placement.NOPE.configure(IPlacementConfig.NO_PLACEMENT_CONFIG));
            b.addFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES, decorated);
            b.addStructure(configured);
        }
    }

}
