package binary404.mystica.common.core.util;

import binary404.mystica.Mystica;
import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.tile.world.TileDungeonCore;
import binary404.mystica.common.tile.world.TileMobCrystal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Mystica.modid, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockLocater {

    @SubscribeEvent
    public static void onBlockBroken(BlockEvent.BreakEvent event) {
        if (event.getState().getBlock() == ModBlocks.dungeon_core) {
            TileDungeonCore tile = (TileDungeonCore) event.getWorld().getTileEntity(event.getPos());
            if (!tile.canBeBroken) {
                System.out.println("Event canceled");
                event.setCanceled(true);
            }
        }
        if (event.getState().getBlock() == ModBlocks.mob_crystal) {
            TileMobCrystal tile = (TileMobCrystal) event.getWorld().getTileEntity(event.getPos());
            if (tile.mobsNearby) {
                System.out.println("Event Canceled");
                event.setCanceled(true);
            }
        }
    }

    public static boolean findBlock(World world, BlockPos startPos, int searchRange, Block block) {
        BlockPos orgin = startPos;
        BlockPos.Mutable pooledPos = new BlockPos.Mutable(orgin.getX(), orgin.getY(), orgin.getZ());
        boolean found = false;
            for (int xx = -searchRange; xx <= searchRange; xx++) {
                for (int zz = -searchRange; zz <= searchRange; zz++) {
                    pooledPos.setPos(orgin.getX() + xx, 0, orgin.getZ() + zz);
                    Chunk c = (Chunk) world.getChunk(pooledPos);
                    int highest = (c.getTopFilledSegment() + 1) * 16;
                    for (int y = 0; y < highest; y++) {
                        pooledPos.setY(y);
                        BlockState state = world.getBlockState(pooledPos);
                        if (state.getBlock() == block) {
                            found = true;
                        }
                    }
                }
            }
        return found;
    }

}
