package binary404.runic.client.libs;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.Matrix4f;

public interface IModelCustom {
    String getType();

    void renderAll();

    void renderOnly(String... paramVarArgs);

    void renderPart(String paramString);

    void renderPart(String paramString, MatrixStack matrix);

    void renderAllExcept(String... paramVarArgs);

    String[] getPartNames();
}
