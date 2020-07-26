package binary404.runic.common.tile;

import binary404.runic.api.ritual.RitualProperties;
import binary404.runic.api.rune.Rune;
import binary404.runic.api.rune.Runes;
import binary404.runic.common.blocks.BlockGlyph;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.datafix.fixes.BlockEntityKeepPacked;
import net.minecraft.util.math.BlockPos;

public class TileRitualRune extends TileMod implements ITickableTileEntity {

    Rune rune;
    RitualProperties prop;
    int ticks = 0;

    public TileRitualRune() {
        this(Runes.LIFE);
    }

    public TileRitualRune(Rune rune) {
        super(ModTiles.RITUAL_GLYPH);
        this.rune = rune;
    }

    @Override
    public void tick() {
        ticks++;
        if (this.prop == null || ticks % 600 == 0) {
            initProperties();
        }
        if (rune.getEffect() != null) {
            rune.getEffect().doEffect(world, pos, prop);
        }
    }

    private void initProperties() {
        int decay = 10;
        int potency = 1;
        int range = 3;
        int stability = 1;
        boolean chaotic = false;
        boolean grounded = false;
        RitualProperties properties = new RitualProperties(range, decay, potency, stability, chaotic, grounded);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                BlockPos pos = this.pos.add(x, 0, z);
                if (world.getBlockState(pos).getBlock() instanceof BlockGlyph) {
                    BlockGlyph glyph = (BlockGlyph) world.getBlockState(pos).getBlock();
                    properties = glyph.modify(properties);
                }
            }
        }
        this.prop = properties;
    }

}
