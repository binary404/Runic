package binary404.runic.api.ritual;

import binary404.runic.api.rune.Rune;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public abstract class RitualEffect {

    protected static final Random rand = new Random();

    private final Rune rune;

    public RitualEffect(Rune rune) {
        this.rune = rune;
    }

    public Rune getRune() {
        return rune;
    }

    public void particles(World world, BlockPos pos, RitualProperties properties) {
    }

    public abstract void doEffect(World world, BlockPos pos, RitualProperties properties);

}
