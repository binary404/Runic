package binary404.mystica.common.container;

import binary404.mystica.Mystica;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

import static binary404.mystica.common.core.util.RegistryUtil.register;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainers {

    private static final List<ContainerType<?>> CONTAINER_TYPES = new ArrayList<>();

    public static final ContainerType<ContainerArcaneWorkbench> arcane_workbench = register("arcane_workbench", ContainerArcaneWorkbench::create);

    static <T extends Container> ContainerType<T> register(final String name, final IContainerFactory<T> factory) {
        final ContainerType<T> containerType = IForgeContainerType.create(factory);
        containerType.setRegistryName(name);
        CONTAINER_TYPES.add(containerType);
        return containerType;
    }

    @SubscribeEvent
    public static void onRegistry(final RegistryEvent.Register<ContainerType<?>> event) {
        CONTAINER_TYPES.forEach(block -> event.getRegistry().register(block));
    }

}
