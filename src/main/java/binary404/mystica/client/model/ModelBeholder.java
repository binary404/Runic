package binary404.mystica.client.model;
import binary404.mystica.common.entity.hostile.EntityBeholder;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelBeholder extends EntityModel<EntityBeholder> {
    private final ModelRenderer main;

    public ModelBeholder() {
        textureWidth = 64;
        textureHeight = 64;

        main = new ModelRenderer(this);
        main.setRotationPoint(6.0F, 21.0F, -6.0F);
        main.setTextureOffset(0, 2).addBox(-10.0F, -14.0F, 4.0F, 8.0F, 2.0F, 5.0F, 0.0F, false);
        main.setTextureOffset(55, 57).addBox(-4.0F, -15.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        main.setTextureOffset(56, 59).addBox(-10.0F, -15.0F, -2.0F, 2.0F, 2.0F, 2.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-11.0F, -13.0F, -3.0F, 10.0F, 2.0F, 10.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-4.0F, -8.0F, -3.0F, 1.0F, 4.0F, 3.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-10.0F, -11.0F, -4.0F, 8.0F, 2.0F, 4.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-9.0F, -8.0F, -3.0F, 1.0F, 4.0F, 3.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-9.0F, -4.0F, -3.0F, 6.0F, 1.0F, 3.0F, 0.0F, false);
        main.setTextureOffset(0, 0).addBox(-9.0F, -9.0F, -3.0F, 6.0F, 1.0F, 3.0F, 0.0F, false);
        main.setTextureOffset(54, 57).addBox(-8.0F, -8.0F, -1.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        main.setTextureOffset(12, 34).addBox(-11.0F, -12.0F, 0.0F, 10.0F, 10.0F, 9.0F, 0.0F, false);
    }

    @Override
    public void setRotationAngles(EntityBeholder entity, float v, float v1, float v2, float v3, float v4) {

    }

    @Override
    public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}