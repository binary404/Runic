package binary404.runic.common.core.event;

import binary404.runic.Runic;
import binary404.runic.common.blocks.ModBlocks;
import binary404.runic.common.items.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Runic.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class GuideHandler {

    private static Map<ItemEntity, Integer> items = new HashMap<ItemEntity, Integer>();

    @SubscribeEvent
    public static void itemHandle(TickEvent.WorldTickEvent event) {
        World world = event.world;
        if (!items.isEmpty()) {
            try {
                items.forEach((item, timeLeft) -> {
                    if (!item.isAlive()) {
                        items.remove(item);
                    } else if (timeLeft > 1) {
                        items.put(item, timeLeft - 1);
                    } else {
                        BlockPos pos = item.getPosition();
                        if (world.getBlockState(pos).getBlock() == Blocks.ENCHANTING_TABLE || world.getBlockState(pos.down()).getBlock() == Blocks.ENCHANTING_TABLE) {
                            if (item.getItem().getCount() == 1)
                                item.setItem(new ItemStack(ModItems.guide));
                        }
                        items.remove(item);
                    }
                });
            } catch (ConcurrentModificationException e) {
                e.printStackTrace();
            }
        }
    }

    @SubscribeEvent
    public static void itemThrown(ItemTossEvent event) {
        System.out.println("item Tossed");
        if (Block.getBlockFromItem(event.getEntityItem().getItem().getItem()) == Blocks.STONE) {
            items.put(event.getEntityItem(), 100);
            System.out.println("Item added");
        }
    }

}
