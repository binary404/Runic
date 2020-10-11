package binary404.mystica.data;

import binary404.mystica.Mystica;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MysticaDataGenerator {

    @SubscribeEvent
    public static void gather(GatherDataEvent event) {
        net.minecraft.data.DataGenerator gen = event.getGenerator();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        if (event.includeServer()) {
            gen.addProvider(new RecipeProvider(gen));
            gen.addProvider(new BlockLootProvider(gen));
        } else {
            gen.addProvider(new MysticaBlockStateProvider(gen, fileHelper));
        }
    }

}
