package binary404.runic.client.render.tile;

import binary404.runic.common.tile.TileArcaneForge;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

public class RenderArcaneForge extends TileEntityRenderer<TileArcaneForge> {

    public RenderArcaneForge(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(TileArcaneForge tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (tileEntityIn != null && !tileEntityIn.getStack().isEmpty()) {
            matrixStackIn.push();
            ItemEntity item = null;
            matrixStackIn.translate(0.5, 1.01D, 0.25);
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
            ItemStack is = tileEntityIn.getStack().copy();
            item = new ItemEntity(tileEntityIn.getWorld(), tileEntityIn.getPos().getX(), tileEntityIn.getPos().getY(), tileEntityIn.getPos().getZ(), is);
            item.hoverStart = 0.0F;
            Minecraft.getInstance().getRenderManager().renderEntityStatic(item, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, matrixStackIn, bufferIn, combinedLightIn);
            matrixStackIn.pop();
        }
    }
}
