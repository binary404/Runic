package binary404.mystica;

import binary404.mystica.common.config.RecipeConfig;
import binary404.mystica.common.config.ResearchConfig;
import binary404.mystica.common.core.capability.CapabilityEvent;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.entity.hostile.EntityBeholder;
import binary404.mystica.common.entity.hostile.EntityCultZombie;
import binary404.mystica.common.entity.ModEntities;
import binary404.mystica.proxy.ClientProxy;
import binary404.mystica.proxy.IProxy;
import binary404.mystica.proxy.ServerProxy;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("mystica")
public class Mystica {

    public static Mystica instance;
    public static IProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger("RUNIC");

    public static final String modid = "mystica";

    public Mystica() {
        instance = this;
        proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        proxy.registerEventHandlers();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(FMLCommonSetupEvent event) {
        RecipeConfig.init();
        CapabilityEvent.init();
        PacketHandler.init();
        ResearchConfig.init();
        DeferredWorkQueue.runLater(() -> {
            ResearchConfig.post();

            GlobalEntityTypeAttributes.put(ModEntities.CULT_ZOMBIE, EntityCultZombie.attributes().create());
            GlobalEntityTypeAttributes.put(ModEntities.RANGED_CULT_ZOMBIE, EntityCultZombie.attributes().create());
            GlobalEntityTypeAttributes.put(ModEntities.BEHOLDER, EntityBeholder.attributes().create());
        });
    }

    public static ResourceLocation key(String path) {
        return new ResourceLocation("mystica", path);
    }

}
