package binary404.mystica.common.tile.world;

import binary404.mystica.common.blocks.ModBlocks;
import binary404.mystica.common.core.util.BlockLocater;
import binary404.mystica.common.tile.ModTiles;
import binary404.mystica.common.tile.TileMod;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;

public class TileDungeonCore extends TileMod implements ITickableTileEntity {

    public boolean canBeBroken = false;

    private Thread searcher;
    private int ticks = 0;

    public TileDungeonCore() {
        super(ModTiles.DUNGEON_CORE);
    }

    @Override
    public void tick() {
        ticks++;
        BlockPos search = this.pos;
        if (this.ticks % 600 == 0) {
            searcher = new Thread(() -> {
                boolean able = BlockLocater.findBlock(world, search, 64, ModBlocks.mob_crystal);
                if (!able) {
                    canBeBroken = true;
                }
            });
            searcher.start();
        }
    }
}
