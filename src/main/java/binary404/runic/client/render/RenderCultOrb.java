package binary404.runic.client.render;

import binary404.runic.client.libs.RenderTypes;
import binary404.runic.common.entity.EntityCultOrb;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;

import javax.swing.text.html.parser.Entity;
import java.util.Random;

public class RenderCultOrb<T extends EntityCultOrb> extends EntityRenderer<T> {

    public RenderCultOrb(EntityRendererManager p_i46179_1_) {
        super(p_i46179_1_);
    }


    @Override
    public void render(T entity, float p_225623_2_, float p_225623_3_, MatrixStack stack, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
        super.render(entity, p_225623_2_, p_225623_3_, stack, p_225623_5_, p_225623_6_);
        stack.push();
        float f3 = 0.9F;
        float f2 = 0.0F;
        IVertexBuilder buffer = p_225623_5_.getBuffer(RenderTypes.orb);
        Matrix4f matrix = stack.getLast().getMatrix();
        System.out.println("Rendering");
        f2 = (entity.ticksExisted % 13) / 16.0F;
        f3 = f2 + 0.062437F;
        float f4 = 0.0F;
        float f5 = f4 + 0.0624375F;
        float f6 = 1.0F;
        float f7 = 0.5F;
        float f8 = 0.5F;

        stack.rotate(renderManager.getCameraOrientation());
        stack.rotate(Vector3f.YP.rotationDegrees(180));

        buffer.pos(matrix, (0.0F - f7), (0.0F - f8), 0.0F).color(.0F, 1.0F, 1.0F, 1.0F).tex(f2, f5).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, (f6 - f7), (0.0F - f8), 0.0F).color(.0F, 1.0F, 1.0F, 1.0F).tex(f3, f5).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, (f6 - f7), (1.0F - f8), 0.0F).color(.0F, 1.0F, 1.0F, 1.0F).tex(f3, f4).normal(0.0F, 1.0F, 0.0F).endVertex();
        buffer.pos(matrix, (0.0F - f7), (1.0F - f8), 0.0F).color(.0F, 1.0F, 1.0F, 1.0F).tex(f2, f4).normal(0.0F, 1.0F, 0.0F).endVertex();

        stack.pop();
    }

    @Override
    public ResourceLocation getEntityTexture(EntityCultOrb entity) {
        return new ResourceLocation("runic", "textures/entity/orbs.png");
    }
}
