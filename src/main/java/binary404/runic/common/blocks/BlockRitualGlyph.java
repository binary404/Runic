package binary404.runic.common.blocks;

import binary404.runic.api.rune.Rune;
import binary404.runic.common.tile.TileRitualRune;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class BlockRitualGlyph extends BlockGlyph {

    public BlockRitualGlyph(Block.Properties properties, Rune rune) {
        super(properties, rune);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return !rune.isPrimal();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TileRitualRune(this.rune);
    }
}
