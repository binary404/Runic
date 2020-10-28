package binary404.mystica.client.core.handler;

import binary404.mystica.common.items.ItemWand;
import binary404.mystica.common.items.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;

public class ColorHandler {

    public static void init() {
        ItemColors items = Minecraft.getInstance().getItemColors();

        items.register((s, t) -> {
            if (t == 0) {
                return ((ItemWand) s.getItem()).getCap(s).getColor();
            }
            if (t == 1) {
                return ((ItemWand) s.getItem()).getRod(s).getColor();
            }
            return -1;
        }, ModItems.wand);
    }

}
