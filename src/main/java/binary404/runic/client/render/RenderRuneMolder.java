package binary404.runic.client.render;

import binary404.runic.client.libs.AdvancedModelLoader;
import binary404.runic.client.libs.IModelCustom;
import binary404.runic.client.libs.RenderTypes;
import binary404.runic.common.tile.TileRuneMolder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class RenderRuneMolder extends TileEntityRenderer<TileRuneMolder> {

    private static final ResourceLocation TM = new ResourceLocation("runic", "models/block/rune_molder.obj");
    private IModelCustom model = AdvancedModelLoader.loadModel(TM);


    public RenderRuneMolder(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileRuneMolder tile, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        stack.push();
        stack.translate(0.5F, 0, 0.5F);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.renderDispatcher.textureManager.bindTexture(new ResourceLocation("textures/blocks/rune_molder.png"));

        IVertexBuilder buffer = bufferIn.getBuffer(RenderTypes.OBJ);

        this.model.renderPart("table", buffer);
        stack.push();
        float h = tile.press;
        double s = Math.sin(Math.toRadians(h)) * 0.625D;
        stack.translate(0.0D, -s, 0.0D);
        this.model.renderPart("press", buffer);
        stack.pop();
        stack.pop();
    }
}
