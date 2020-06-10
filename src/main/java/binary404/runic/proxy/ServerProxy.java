package binary404.runic.proxy;

import binary404.runic.common.config.ResearchConfig;
import binary404.runic.common.core.capability.CapabilityEvent;
import binary404.runic.common.core.network.PacketHandler;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ServerProxy implements IProxy {

    @Override
    public void registerEventHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
    }
}
