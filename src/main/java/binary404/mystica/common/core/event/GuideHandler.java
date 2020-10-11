package binary404.mystica.common.core.event;

import binary404.mystica.Mystica;
import binary404.mystica.client.fx.FXHelper;
import binary404.mystica.common.core.network.PacketHandler;
import binary404.mystica.common.core.network.fx.PacketMysticaFX;
import binary404.mystica.common.items.ModItems;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuideHandler {

    private static Map<ItemEntity, Integer> items = new HashMap<ItemEntity, Integer>();

    @SubscribeEvent
    public static void itemHandle(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (!items.isEmpty()) {
            List<ItemEntity> toRemove = new ArrayList<>();
            items.forEach((item, timeLeft) -> {
                if (timeLeft > 1) {
                    items.put(item, timeLeft - 1);
                    if (world.getBlockState(item.getPosition()).getBlock() == Blocks.ENCHANTING_TABLE || world.getBlockState(item.getPosition().down()).getBlock() == Blocks.ENCHANTING_TABLE)
                        if (world.rand.nextInt(10) == 0)
                            PacketHandler.sendToNearby(world, item, new PacketMysticaFX(item.getPosX(), item.getPosY() + 1.5, item.getPosZ(), 2));
                } else {
                    item.setItem(new ItemStack(ModItems.guide));

                    FXHelper.poof(item.getPosX(), item.getPosY() + 0.5, item.getPosZ(), 0.8F, 0.2F, 0.8F, 0.7F);
                    FXHelper.flare(item.getPosX(), item.getPosY() + 0.5, item.getPosZ(), 1.0F, 0.9F, 1.0F, 1.2F);

                    toRemove.add(item);
                }
            });
            try {
                for (ItemEntity item : toRemove) {
                    items.remove(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void itemThrown(ItemTossEvent event) {
        if (event.getEntityItem().getItem().getItem() == Items.BOOK && event.getEntityItem().getItem().getCount() == 1 && event.getEntityItem().isAlive()) {
            items.put(event.getEntityItem(), 300);
        }
    }

}
