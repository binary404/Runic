package binary404.runic.client.gui.container;

import binary404.runic.client.utils.RenderingUtils;
import binary404.runic.common.container.RuneMolderContainer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

public class GuiRuneMolder extends ContainerScreen<RuneMolderContainer> {

    private RuneMolderContainer container;

    public GuiRuneMolder(RuneMolderContainer container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title);
        this.container = container;
        this.xSize = 176;
        this.ySize = 192;
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        super.render(p_render_1_, p_render_2_, p_render_3_);
        this.renderHoveredToolTip(p_render_1_, p_render_2_);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        int progress = this.container.tileEntity.progress * 48 / 600;
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        drawFluid(68,  31, this.container.tileEntity.tank.getFluid(), progress, 6);
    }

    @Override
    protected void renderHoveredToolTip(int mouseX, int mouseY) {
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.minecraft.getTextureManager().bindTexture(new ResourceLocation("runic", "textures/gui/gui_rune_molder.png"));
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (this.width - this.xSize) / 2;
        int y = (this.height - this.ySize) / 2;
        blit(x, y, 0, 0, 176, 192);
        int amount = this.container.tileEntity.tank.getFluidAmount() * 56 / this.container.tileEntity.tank.getCapacity();
        drawFluid(x + 16, y + 8 + 56 - amount, this.container.tileEntity.tank.getFluid(), 16, amount);
    }

    public void drawFluid(int x, int y, FluidStack fluid, int width, int height) {
        if (fluid == null)
            return;
        RenderSystem.pushMatrix();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderingUtils.setBlockTextureSheet();
        RenderingUtils.setGLColorFromInt(fluid.getFluid().getAttributes().getColor());
        drawTiledTexture(x, y, RenderingUtils.getTexture(fluid.getFluid().getAttributes().getStillTexture()), width, height);
        RenderSystem.popMatrix();
    }

    public void drawTiledTexture(int x, int y, TextureAtlasSprite icon, int width, int height) {
        int i, j;
        int drawHeight, drawWidth;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = Math.min(width - i, 16);
                drawHeight = Math.min(height - j, 16);
                blitFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
            }
        }
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    public void blitFromIcon(int x, int y, TextureAtlasSprite sprite, int width, int height) {
        if (sprite == null)
            return;

        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        buffer.pos(x, y + height, this.getBlitOffset()).tex(minU, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y + height, this.getBlitOffset()).tex(minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F).endVertex();
        buffer.pos(x + width, y, this.getBlitOffset()).tex(minU + (maxU - minU) * width / 16F, minV).endVertex();
        buffer.pos(x, y, this.getBlitOffset()).tex(minU, minV).endVertex();
        Tessellator.getInstance().draw();
    }
}
