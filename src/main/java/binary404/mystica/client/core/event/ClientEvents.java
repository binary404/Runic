package binary404.mystica.client.core.event;

import binary404.mystica.Mystica;
import binary404.mystica.client.fx.FXHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientEvents {

    public static float currentMystic;
    public static float currentFlux;

    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().world != null) {
            RegistryKey<World> dimension = Minecraft.getInstance().world.getDimensionKey();
            PlayerEntity player = Minecraft.getInstance().player;
            if (player != null && dimension != null) {
                if (currentMystic >= 350) {
                    for (int x = -10; x <= 10; x++) {
                        for (int z = -10; z <= 10; z++) {
                            BlockPos pos = player.getPosition().add(x, 0, z);
                            Random rand = new Random();
                            if (Minecraft.getInstance().world.rand.nextInt(40) == 0)
                                FXHelper.sparkle(pos.getX() + 0.5 + rand.nextGaussian() * 0.8, pos.getY() + 0.5 + rand.nextGaussian() * 0.8, pos.getZ() + 0.5 + rand.nextGaussian() * 0.8, rand.nextGaussian() * 0.1, 0.15, rand.nextGaussian() * 0.1, 0.5F, 0.3F, 0.7F, 0.3F, 1.0F, 0.0F, 15);
                        }
                    }
                }
            }
        }
    }

}
