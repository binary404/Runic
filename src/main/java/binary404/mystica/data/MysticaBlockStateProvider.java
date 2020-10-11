package binary404.mystica.data;

import binary404.mystica.Mystica;
import binary404.mystica.common.blocks.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class MysticaBlockStateProvider extends BlockStateProvider {

    public MysticaBlockStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper) {
        super(gen, Mystica.modid, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlockState(ModBlocks.runed_stone);
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
