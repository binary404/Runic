package binary404.runic.client.render;

import binary404.runic.Runic;
import binary404.runic.client.model.ModelBeholder;
import binary404.runic.common.entity.EntityBeholder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class RenderBeholder extends MobRenderer<EntityBeholder, ModelBeholder> {

    public RenderBeholder(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new ModelBeholder(), 0.0f);
    }

    @Override
    protected void preRenderCallback(EntityBeholder p_225620_1_, MatrixStack p_225620_2_, float p_225620_3_) {
        super.preRenderCallback(p_225620_1_, p_225620_2_, p_225620_3_);
        p_225620_2_.scale(1.6F, 1.6F, 1.6F);
    }

    @Override
    public ResourceLocation getEntityTexture(EntityBeholder entity) {
        return new ResourceLocation(Runic.modid, "textures/entity/beholder.png");
    }
}
