package binary404.mystica.common.container;

import binary404.mystica.Mystica;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> r = event.getRegistry();
    }

}
