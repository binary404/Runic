package binary404.runic.common.container;

import binary404.runic.Runic;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import static binary404.runic.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    @ObjectHolder("runic:rune_molder")
    public static ContainerType<RuneMolderContainer> RUNE_MOLDER;

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        IForgeRegistry<ContainerType<?>> r = event.getRegistry();
        register(r, IForgeContainerType.create(RuneMolderContainer::new), "rune_molder");
    }

}
