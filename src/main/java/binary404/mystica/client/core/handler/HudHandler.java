package binary404.mystica.client.core.handler;

import binary404.mystica.api.item.IWeaveViewer;
import binary404.mystica.client.core.handler.RenderTypes;
import binary404.mystica.client.utils.RenderingUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.text.DecimalFormat;

public class HudHandler {

    public float mystic;
    public short base;
    public float flux;

    final ResourceLocation HUD = new ResourceLocation("mystica", "textures/gui/hud.png");
    public DecimalFormat secondsFormatter = new DecimalFormat("#######.#");

    public void renderHuds(Minecraft mc, float renderTickTime, PlayerEntity player, long time) {
        RenderSystem.pushMatrix();
        GL11.glClear(256);
        RenderSystem.matrixMode(5889);
        RenderSystem.loadIdentity();
        RenderSystem.ortho(0.0D, mc.getMainWindow().getScaledWidth(), mc.getMainWindow().getScaledHeight(), 0.0D, 1000.0D, 3000.0D);
        RenderSystem.matrixMode(5888);
        RenderSystem.loadIdentity();
        RenderSystem.translated(0.0D, 0.0D, -2000.0D);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 771);
        ItemStack handStack = player.getHeldItemMainhand();
        if (handStack.getItem() instanceof IWeaveViewer) {
            renderMysticometerHud(mc, renderTickTime, player);
        }
        RenderSystem.popMatrix();
    }

    void renderMysticometerHud(Minecraft mc, float partialTicks, PlayerEntity player) {
        float base = MathHelper.clamp(this.base / 525.0F, 0.0F, 1.0F);
        float mystic = MathHelper.clamp(this.mystic / 525.0F, 0.0F, 1.0F);
        float flux = MathHelper.clamp(this.flux / 525.0F, 0.0F, 1.0F);
        float count = mc.getRenderViewEntity().ticksExisted + partialTicks;
        float count2 = mc.getRenderViewEntity().ticksExisted / 3.0F + partialTicks;

        IRenderTypeBuffer.Impl buffers = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());

        IVertexBuilder buffer = buffers.getBuffer(RenderTypes.getHud());

        if (flux + mystic > 1.0F) {
            float m = 1.0F / (flux + mystic);
            base *= m;
            mystic *= m;
            flux *= m;
        }

        Minecraft.getInstance().getTextureManager().bindTexture(HUD);

        float start = 10.0F + (1.0F - mystic) * 64.0F;
        RenderSystem.pushMatrix();
        RenderSystem.blendFunc(770, 771);
        MatrixStack stack = new MatrixStack();
        stack.translate(2.0, 0.0, 0.0);
        if (mystic > 0.0F) {

            stack.push();
            stack.translate(5.0, start, 0.0);
            stack.scale(1.0F, mystic, 1.0F);
            RenderingUtils.drawTexturedQuad(0, 0, 20.0F, 9.0F, 8.0F, 64.0F, -90.0F, 0.7F, 0.4F, 0.9F, 1.0F, buffer, stack);
            stack.pop();

            stack.push();
            RenderSystem.blendFunc(770, 1);
            stack.translate(5.0, start, 0.0);
            RenderingUtils.drawTexturedQuad(0, 0, 96.0F, 56.0F + count % 64.0F, 8.0F, mystic * 64.0F, -90.0F, 1.0F, 1.0F, 1.0F, 1.0F, buffer, stack);
            buffers.finish(RenderTypes.getHud());
            RenderSystem.blendFunc(770, 771);
            stack.pop();

            if (player.isSneaking()) {
                stack.push();
                stack.translate(2.0D, 0.0D, 0.D);
                stack.translate(16.0D, (start), 0.0D);
                stack.scale(0.5F, 0.5F, 0.5F);
                String msg = this.secondsFormatter.format(this.mystic);
                mc.fontRenderer.drawString(stack, msg, 0, 0, 11145659);
                stack.pop();
                mc.getTextureManager().bindTexture(HUD);
            }
            buffer = buffers.getBuffer(RenderTypes.getHud());
        }

        if (flux > 0.0F) {

            start = 10.0F + (1.0F - flux - mystic) * 64.0F;

            stack.push();
            stack.translate(5.0, start, 0.0);
            stack.scale(1.0F, flux, 1.0F);
            RenderingUtils.drawTexturedQuad(0, 0, 20.0F, 9.0F, 8.0F, 64.0F, -90.0F, 0.25F, 0.1F, 0.3F, 1.0F, buffer, stack);
            stack.pop();

            stack.push();
            RenderSystem.blendFunc(770, 1);
            stack.translate(5.0, start, 0.0);
            RenderingUtils.drawTexturedQuad(0.0F, 0.0F, 104.0F, 120.0F - count2 % 64.0F, 8.0F, flux * 64, -90.0F, 0.7F, 0.4F, 1.0F, 0.5F, buffer, stack);
            RenderSystem.blendFunc(770, 771);
            buffers.finish(RenderTypes.getHud());
            stack.pop();

            if (player.isSneaking()) {
                stack.push();
                stack.translate(2.0D, 0.0D, 0.0D);
                stack.translate(16.0D, (start - 4.0F), 0.0D);
                stack.scale(0.5F, 0.5F, 0.5F);
                String msg = this.secondsFormatter.format(this.flux);
                mc.fontRenderer.drawString(stack, msg, 0, 0, 11145659);
                stack.pop();
                mc.getTextureManager().bindTexture(HUD);
            }

            buffer = buffers.getBuffer(RenderTypes.getHud());

        }

        RenderingUtils.drawTexturedQuad(1.0F, 1.0F, 1.0F, 3.0F, 16.0F, 80.0F, -90.0F, 1.0F, 1.0F, 1.0F, 1.0F, buffer, stack);

        start = 8.0F + (1.0F - base) * 64.0F;
        RenderingUtils.drawTexturedQuad(2.0F, start, 0.0F, 84.0F, 14.0F, 5.0F, -90.F, 1.0F, 1.0F, 1.0F, 1.0F, buffer, stack);
        buffers.finish(RenderTypes.getHud());

        RenderSystem.popMatrix();
    }

}
