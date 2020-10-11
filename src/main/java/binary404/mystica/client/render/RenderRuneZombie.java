package binary404.mystica.client.render;

import binary404.mystica.client.core.handler.RenderTypes;
import binary404.mystica.client.model.ModelCultZombie;
import binary404.mystica.common.entity.hostile.EntityCultZombie;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.BipedRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;


public class RenderRuneZombie extends BipedRenderer<EntityCultZombie, ModelCultZombie<EntityCultZombie>> {

    public RenderRuneZombie(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelCultZombie(0.0F), 0.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCultZombie entity) {
        return new ResourceLocation("mystica", "textures/entity/rune_zombie.png");
    }

    @Override
    public void render(EntityCultZombie entityIn, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        RenderSystem.pushMatrix();
        if (entityIn.world.isDaytime()) {
            RenderSystem.blendFunc(770, 771);
            RenderSystem.alphaFunc(516, 0.003921569F);
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 0.6F);
        }
        super.render(entityIn, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        RenderSystem.popMatrix();
        float scale = entityIn.auraSize;
        stack.push();
        stack.rotate(Vector3f.XP.rotationDegrees(90F));
        stack.translate(-(scale / 2), -(scale / 2), 0);
        stack.translate(scale / 2, scale / 2, 0);
        stack.rotate(Vector3f.ZP.rotationDegrees(entityIn.ticksExisted));
        stack.translate(-(scale / 2), -(scale / 2), 0);
        stack.scale(scale, scale, 0);
        Matrix4f mat = stack.getLast().getMatrix();
        IVertexBuilder buffer = bufferIn.getBuffer(RenderTypes.getTEOverlay(new ResourceLocation("mystica", "textures/misc/rune_molder.png")));
        buffer.pos(mat, 0, 0 + 1, 0).color(1, 1, 1, 0.8F).tex(0, 1).endVertex();
        buffer.pos(mat, 0 + 1, 0 + 1, 0).color(1, 1, 1, 0.8F).tex(1, 1).endVertex();
        buffer.pos(mat, 0 + 1, 0, 0).color(1, 1, 1, 0.8F).tex(1, 0).endVertex();
        buffer.pos(mat, 0, 0, 0).color(1, 1, 1, 0.8F).tex(0, 0).endVertex();
        stack.pop();
    }
}
