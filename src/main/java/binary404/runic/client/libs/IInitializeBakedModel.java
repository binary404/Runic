package binary404.runic.client.libs;

import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.util.ResourceLocation;

public interface IInitializeBakedModel {

    void initialize(ItemCameraTransforms p0, final ResourceLocation p1, final ModelManager p2);

}
