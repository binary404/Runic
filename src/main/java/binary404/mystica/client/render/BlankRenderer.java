package binary404.mystica.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class BlankRenderer<T extends Entity> extends EntityRenderer<T> {

    public BlankRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public void render(T entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull T entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

}
