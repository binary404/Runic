package binary404.runic.data;

import binary404.runic.Runic;
import binary404.runic.common.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RunicBlockStateProvider extends BlockStateProvider {

    public RunicBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Runic.modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockState(ModBlocks.runed_stone);
        this.simpleBlockState(ModBlocks.arcane_anvil);
        this.simpleBlockState(ModBlocks.storage);
        this.simpleBlockState(ModBlocks.furnace);

        this.simpleBlockState(ModBlocks.glyph_earth);
        this.simpleBlockState(ModBlocks.glyph_fire);
        this.simpleBlockState(ModBlocks.glyph_air);
        this.simpleBlockState(ModBlocks.glyph_water);
        this.simpleBlockState(ModBlocks.glyph_chaos);
        this.simpleBlockState(ModBlocks.glyph_order);
    }

    private void simpleBlockState(Block b) {
        this.simpleBlockState(b, model(b.getRegistryName()));
    }

    private void simpleBlockState(Block b, ModelFile targetModel) {
        getVariantBuilder(b).partialState().addModels(new ConfiguredModel(targetModel));
    }

    private ModelFile model(IForgeRegistryEntry<?> entry) {
        return model(entry.getRegistryName());
    }

    private ModelFile model(ResourceLocation name) {
        return new ModelFile.UncheckedModelFile(prefixPath(name, "block/"));
    }

    public static ResourceLocation prefixPath(ResourceLocation key, String prefix) {
        return new ResourceLocation(key.getNamespace(), prefix + key.getPath());
    }

}
