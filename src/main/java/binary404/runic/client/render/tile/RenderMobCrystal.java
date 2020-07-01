package binary404.runic.client.render.tile;

import binary404.runic.Runic;
import binary404.runic.common.tile.world.TileMobCrystal;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class RenderMobCrystal extends TileEntityRenderer<TileMobCrystal> {

    private static final RenderType LAYER = RenderType.getEntityCutoutNoCull(new ResourceLocation(Runic.modid, "textures/block/mob_crystal.png"));
    private static final float ANGLE = (float) Math.sin(Math.toRadians(45));
    private final ModelRenderer cube;

    public RenderMobCrystal(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
        this.cube = new ModelRenderer(64, 32, 32, 0);
        this.cube.addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F);
    }

    @Override
    public void render(TileMobCrystal tileEntityIn, float partialTicks, MatrixStack ms, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        ms.push();
        IVertexBuilder buffer = bufferIn.getBuffer(LAYER);
        ms.translate(0.5, 0, 0.5);
        ms.push();
        ms.translate(0.0D, -1, 0.0D);
        ms.rotate(Vector3f.YP.rotationDegrees(tileEntityIn.ticks));
        ms.translate(0.0D, 1.5F, 0.0D);
        ms.rotate(new Quaternion(new Vector3f(ANGLE, 0.0F, ANGLE), 60.0F, true));
        ms.scale(0.875F, 0.875F, 0.875F);
        this.cube.render(ms, buffer, combinedLightIn, combinedOverlayIn);
        ms.pop();
        ms.pop();
    }
}
