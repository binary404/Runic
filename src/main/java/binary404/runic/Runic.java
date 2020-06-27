package binary404.runic;

import binary404.runic.common.config.RecipeConfig;
import binary404.runic.common.config.ResearchConfig;
import binary404.runic.common.core.capability.CapabilityEvent;
import binary404.runic.common.core.network.PacketHandler;
import binary404.runic.proxy.ClientProxy;
import binary404.runic.proxy.IProxy;
import binary404.runic.proxy.ServerProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("runic")
public class Runic {

    public static Runic instance;
    public static IProxy proxy;

    public static final Logger LOGGER = LogManager.getLogger("RUNIC");

    public static final String modid = "runic";

    public Runic() {
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
        });
    }

    public static ResourceLocation key(String path) {
        ResourceLocation location = new ResourceLocation("runic", path);
        return new ResourceLocation("runic", path);
    }

}
