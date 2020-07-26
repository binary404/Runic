package binary404.runic.common.items;

import binary404.runic.api.item.IRuneItem;
import binary404.runic.api.rune.Rune;
import net.minecraft.item.Item;

public class ItemBasicRune extends Item implements IRuneItem {

    public Rune rune;

    public ItemBasicRune(Item.Properties properties, Rune rune) {
        super(properties);
        this.rune = rune;
    }


}
