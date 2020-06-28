package binary404.runic.client.render;

import net.minecraft.client.renderer.culling.ClippingHelperImpl;
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
    public boolean shouldRender(T entity, @Nonnull ClippingHelperImpl clipping, double x, double y, double z) {
        return false;
    }

    @Nonnull
    @Override
    public ResourceLocation getEntityTexture(@Nonnull T entity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }

}
