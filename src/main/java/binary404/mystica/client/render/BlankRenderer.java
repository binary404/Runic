package binary404.mystica.client.render;

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
    public boolean shouldRender(T livingEntityIn, ClippingHelper camera, double camX, double camY, double camZ) {
        return false;
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull T entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

}
