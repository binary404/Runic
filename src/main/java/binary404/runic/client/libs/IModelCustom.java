package binary404.runic.client.libs;


import com.mojang.blaze3d.vertex.IVertexBuilder;

public interface IModelCustom {
    String getType();

    void renderAll();

    void renderOnly(String... paramVarArgs);

    void renderPart(String paramString);

    void renderPart(String paramString, IVertexBuilder buffer);

    void renderAllExcept(String... paramVarArgs);

    String[] getPartNames();
}
