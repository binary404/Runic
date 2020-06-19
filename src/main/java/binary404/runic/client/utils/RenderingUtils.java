package binary404.runic.client.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.prism.TextureMap;
import net.java.games.input.Mouse;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RenderingUtils {

    public static boolean renderItemStack(Minecraft mc, ItemStack itm, int x, int y, String txt) {
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        ItemRenderer itemRender = mc.getItemRenderer();
        boolean isLightingEnabled = GL11.glIsEnabled(2896);

        boolean rc = false;

        if (itm != null && !itm.isEmpty()) {
            rc = true;
            boolean isRescaleNormalEnabled = GL11.glIsEnabled(32826);
            RenderSystem.pushMatrix();
            RenderSystem.translatef(0.0F, 0.0F, 32.0F);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(32826);
            GL11.glEnable(2896);
            short short1 = 240;
            short short2 = 240;
            RenderHelper.enableStandardItemLighting();
            itemRender.renderItemAndEffectIntoGUI(itm, x, y);
            itemRender.renderItemOverlayIntoGUI(mc.fontRenderer, itm, x, y, txt);
            RenderSystem.popMatrix();
            if (isRescaleNormalEnabled) {
                GL11.glEnable(32826);
            } else {
                GL11.glDisable(32826);
            }
        }

        if (isLightingEnabled) {
            GL11.glEnable(2896);
        } else {
            GL11.glDisable(2896);
        }

        return rc;
    }

    public static int getScaleFactor(MainWindow window) {
        int scaledWidth = window.getScaledWidth();
        int scaledHeight = window.getScaledHeight();
        int scaleFactor = 1;

        int i = Minecraft.getInstance().gameSettings.guiScale;
        if (i == 0)
            i = 1000;

        while (scaleFactor < i && scaledWidth / (scaleFactor + 1) >= 320 && scaledHeight / (scaleFactor + 1) >= 240) {
            ++scaleFactor;
        }

        if (scaleFactor % 2 != 0 && scaleFactor != 1) {
            --scaleFactor;
        }

        return scaleFactor;
    }

    public static void drawTexturedQuadFull(float par1, float par2, double zLevel) {
        Tessellator var9 = Tessellator.getInstance();
        var9.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
        var9.getBuffer().pos((par1 + 0.0F), (par2 + 16.0F), zLevel).tex(0.F, 1.0F).endVertex();
        var9.getBuffer().pos((par1 + 16.0F), (par2 + 16.0F), zLevel).tex(1.0F, 1.0F).endVertex();
        var9.getBuffer().pos((par1 + 16.0F), (par2 + 0.0F), zLevel).tex(1.0F, 0.0F).endVertex();
        var9.getBuffer().pos((par1 + 0.0F), (par2 + 0.0F), zLevel).tex(0.0F, 0.0F).endVertex();
        var9.draw();
    }

    public static void drawTexturedQuadF(float par1, float par2, float par3, float par4, float par5, float par6, double zLevel) {
        float d = 0.0625F;
        Tessellator var9 = Tessellator.getInstance();
        var9.getBuffer().begin(7, DefaultVertexFormats.POSITION_TEX);
        var9.getBuffer().pos((par1 + 0.0F), (par2 + 16.0F), zLevel).tex(((par3 + 0.0F) * d), ((par4 + par6) * d)).endVertex();
        var9.getBuffer().pos((par1 + 16.0F), (par2 + 16.0F), zLevel).tex(((par3 + par5) * d), ((par4 + par6) * d)).endVertex();
        var9.getBuffer().pos((par1 + 16.0F), (par2 + 0.0F), zLevel).tex(((par3 + par5) * d), ((par4 + 0.0F) * d)).endVertex();
        var9.getBuffer().pos((par1 + 0.0F), (par2 + 0.0F), zLevel).tex(((par3 + 0.0F) * d), ((par4 + 0.0F) * d)).endVertex();
        var9.draw();
    }

    public static void drawCustomTooltip(Screen gui, FontRenderer fr, List textList, int x, int y) {
        drawCustomTooltip(gui, fr, textList, x, y, 0xFFFF, false);
    }

    public static void drawCustomTooltip(Screen gui, FontRenderer fr, List textList, int x, int y, int subTipColor, boolean ignoremouse) {
        if (!textList.isEmpty()) {
            Minecraft mc = Minecraft.getInstance();
            int sf = getScaleFactor(mc.getMainWindow());
            RenderSystem.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            RenderSystem.disableLighting();
            RenderSystem.disableDepthTest();
            int max = 240;
            int mx = (int) mc.mouseHelper.getMouseX();
            boolean flip = false;
            if (!ignoremouse && (max + 24) * sf + mx > mc.getMainWindow().getWidth()) {
                max = (mc.getMainWindow().getWidth() - mx) / sf - 24;
                if (max < 120) {
                    max = 240;
                    flip = true;
                }
            }
            int widestLineWidth = 0;
            Iterator textLineEntry = textList.iterator();
            boolean b = false;
            while (textLineEntry.hasNext()) {
                String textLine = (String) textLineEntry.next();
                if (fr.getStringWidth(textLine) > max) {
                    b = true;
                    break;
                }
            }
            if (b) {
                List tl = new ArrayList();
                for (Object o : textList) {
                    String textLine2 = (String) o;
                    List tl2 = fr.listFormattedStringToWidth(textLine2, textLine2.startsWith("@@") ? (max * 2) : max);
                    for (Object o2 : tl2) {
                        String textLine3 = ((String) o2).trim();
                        if (textLine2.startsWith("@@")) {
                            textLine3 = "@@" + textLine3;
                        }
                        tl.add(textLine3);
                    }
                }
                textList = tl;
            }
            Iterator textLines = textList.iterator();
            int totalHeight = -2;
            while (textLines.hasNext()) {
                String textLine4 = (String) textLines.next();
                int lineWidth = fr.getStringWidth(textLine4);
                if (textLine4.startsWith("@@")) {
                    lineWidth /= 2;
                }
                if (lineWidth > widestLineWidth) {
                    widestLineWidth = lineWidth;
                }
                totalHeight += ((textLine4.startsWith("@@")) ? 7 : 10);
            }
            int sX = x + 12;
            int sY = y - 12;
            if (textList.size() > 1) {
                totalHeight += 2;
            }
            if (sY + totalHeight > mc.getMainWindow().getScaledHeight()) {
                sY = mc.getMainWindow().getScaledHeight() - totalHeight - 5;
            }
            if (flip) {
                sX -= widestLineWidth + 24;
            }
            mc.getItemRenderer().zLevel = 300.0f;
            int var10 = -267386864;
            drawGradientRect(sX - 3, sY - 4, sX + widestLineWidth + 3, sY - 3, var10, var10);
            drawGradientRect(sX - 3, sY + totalHeight + 3, sX + widestLineWidth + 3, sY + totalHeight + 4, var10, var10);
            drawGradientRect(sX - 3, sY - 3, sX + widestLineWidth + 3, sY + totalHeight + 3, var10, var10);
            drawGradientRect(sX - 4, sY - 3, sX - 3, sY + totalHeight + 3, var10, var10);
            drawGradientRect(sX + widestLineWidth + 3, sY - 3, sX + widestLineWidth + 4, sY + totalHeight + 3, var10, var10);
            int var11 = 1347420415;
            int var12 = (var11 & 0xFEFEFE) >> 1 | (var11 & 0xFF000000);
            drawGradientRect(sX - 3, sY - 3 + 1, sX - 3 + 1, sY + totalHeight + 3 - 1, var11, var12);
            drawGradientRect(sX + widestLineWidth + 2, sY - 3 + 1, sX + widestLineWidth + 3, sY + totalHeight + 3 - 1, var11, var12);
            drawGradientRect(sX - 3, sY - 3, sX + widestLineWidth + 3, sY - 3 + 1, var11, var11);
            drawGradientRect(sX - 3, sY + totalHeight + 2, sX + widestLineWidth + 3, sY + totalHeight + 3, var12, var12);
            for (int i = 0; i < textList.size(); ++i) {
                RenderSystem.pushMatrix();
                RenderSystem.translatef((float) sX, (float) sY, 0.0f);
                String tl3 = (String) textList.get(i);
                boolean shift = false;
                RenderSystem.pushMatrix();
                if (tl3.startsWith("@@")) {
                    sY += 7;
                    RenderSystem.scalef(0.5f, 0.5f, 1.0f);
                    shift = true;
                } else {
                    sY += 10;
                }
                tl3 = tl3.replaceAll("@@", "");
                RenderSystem.translated(0.0, 0.0, 301.0);
                fr.drawStringWithShadow(tl3, 0.0f, shift ? 3.0f : 0.0f, -1);
                RenderSystem.popMatrix();
                if (i == 0) {
                    sY += 2;
                }
                RenderSystem.popMatrix();
            }
            mc.getItemRenderer().zLevel = 0.0f;
            RenderSystem.enableLighting();
            RenderSystem.enableDepthTest();
            RenderHelper.enableStandardItemLighting();
            RenderSystem.enableRescaleNormal();
        }
    }

    public static void drawGradientRect(int par1, int par2, int par3, int par4, int par5, int par6) {
        boolean blendon = GL11.glIsEnabled(3042);
        float var7 = (par5 >> 24 & 0xFF) / 255.0F;
        float var8 = (par5 >> 16 & 0xFF) / 255.0F;
        float var9 = (par5 >> 8 & 0xFF) / 255.0F;
        float var10 = (par5 & 0xFF) / 255.0F;
        float var11 = (par6 >> 24 & 0xFF) / 255.0F;
        float var12 = (par6 >> 16 & 0xFF) / 255.0F;
        float var13 = (par6 >> 8 & 0xFF) / 255.0F;
        float var14 = (par6 & 0xFF) / 255.0F;
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        RenderSystem.blendFunc(770, 771);
        RenderSystem.shadeModel(7425);
        Tessellator var15 = Tessellator.getInstance();
        var15.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
        var15.getBuffer().pos(par3, par2, 300.0D).color(var8, var9, var10, var7).endVertex();
        var15.getBuffer().pos(par1, par2, 300.0D).color(var8, var9, var10, var7).endVertex();
        var15.getBuffer().pos(par1, par4, 300.0D).color(var12, var13, var14, var11).endVertex();
        var15.getBuffer().pos(par3, par4, 300.0D).color(var12, var13, var14, var11).endVertex();
        var15.draw();
        RenderSystem.shadeModel(7424);
        RenderSystem.blendFunc(770, 771);
        if (!blendon) GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
    }

    public static final ResourceLocation MC_BLOCK_SHEET = new ResourceLocation("textures/atlas/blocks.png");

    public static void setBlockTextureSheet() {
        bindTexture(MC_BLOCK_SHEET);
    }

    public static TextureManager engine() {

        return Minecraft.getInstance().getTextureManager();
    }

    public static void bindTexture(ResourceLocation texture) {

        engine().bindTexture(texture);
    }

    public static TextureAtlasSprite getTexture(ResourceLocation location) {

        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(location);
    }

    public static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        RenderSystem.color4f(red, green, blue, 1.0F);
    }

}
