package binary404.runic.client.model;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.entity.LivingEntity;

public class ModelCultZombie<T extends LivingEntity> extends BipedModel<T> {

    protected ModelCultZombie(float modelSize, float yOffsetIn, int textureWidthIn, int textureHeightIn) {
        super(modelSize, yOffsetIn, textureWidthIn, textureHeightIn);
    }

    public ModelCultZombie(float modelSize) {
        this(modelSize, 0.0F, 64, 64);
    }
}
