package binary404.runic.client.gui.research_extras;

import net.minecraft.util.ResourceLocation;

public class PageImage {

    public int x;
    public int y;
    public int w;
    public int h;
    public int aw;
    public int ah;
    public float scale;
    public ResourceLocation loc;

    public PageImage() {
    }

    public static PageImage parse(String text) {
        String[] s = text.split(":");
        if (s.length != 7) {
            return null;
        }
        try {
            PageImage pi = new PageImage();
            pi.loc = new ResourceLocation(s[0], s[1]);
            pi.x = Integer.parseInt(s[2]);
            pi.y = Integer.parseInt(s[3]);
            pi.w = Integer.parseInt(s[4]);
            pi.h = Integer.parseInt(s[5]);
            pi.scale = Float.parseFloat(s[6]);
            pi.aw = (int) ((float) pi.w * pi.scale);
            pi.ah = (int) ((float) pi.h * pi.scale);
            if (pi.ah > 208 || pi.aw > 140) {
                return null;
            }
            return pi;
        } catch (Exception pi) {
            return null;
        }
    }

}
