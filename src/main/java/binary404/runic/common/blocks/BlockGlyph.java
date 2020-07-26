package binary404.runic.common.blocks;

import binary404.runic.api.ritual.RitualProperties;
import binary404.runic.api.rune.Rune;
import binary404.runic.api.rune.Runes;
import net.minecraft.block.Block;

public class BlockGlyph extends Block {

    public Rune rune;

    public BlockGlyph(Block.Properties properties, Rune rune) {
        super(properties);
        this.rune = rune;
    }

    public RitualProperties modify(RitualProperties properties) {
        if (this.rune == Runes.FIRE) {
            return new RitualProperties(properties.size, properties.decay, properties.potency + 1, properties.stability, properties.chaotic, properties.grounded);
        } else if (this.rune == Runes.AIR) {
            return new RitualProperties(properties.size + 1, properties.decay, properties.potency, properties.stability, properties.chaotic, properties.grounded);
        } else if (this.rune == Runes.EARTH) {
            return new RitualProperties(properties.size, properties.decay, properties.potency, properties.stability, properties.chaotic, true);
        } else if (this.rune == Runes.WATER) {
            return new RitualProperties(properties.size, properties.decay - 2, properties.potency, properties.stability, properties.chaotic, properties.grounded);
        } else if (this.rune == Runes.CHAOS) {
            return new RitualProperties(properties.size, properties.decay - 2, properties.potency, properties.stability, true, properties.grounded);
        } else if (this.rune == Runes.ORDER) {
            return new RitualProperties(properties.size, properties.decay, properties.potency, properties.stability + 4, properties.chaotic, properties.grounded);
        }
        return properties;
    }

}
