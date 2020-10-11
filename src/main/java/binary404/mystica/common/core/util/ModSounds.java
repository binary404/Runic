package binary404.mystica.common.core.util;

import binary404.mystica.Mystica;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {

    public static final SoundEvent chant = makeSoundEvent("chant");

    private static SoundEvent makeSoundEvent(String name) {
        ResourceLocation loc = new ResourceLocation(Mystica.modid, name);
        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> r = event.getRegistry();
        r.register(chant);
    }
}
