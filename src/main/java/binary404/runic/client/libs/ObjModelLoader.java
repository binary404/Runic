package binary404.runic.client.libs;

import net.minecraft.util.ResourceLocation;

public class ObjModelLoader implements IModelCustomLoader {
    public String getType() {
        return "OBJ model";
    }


    private static final String[] types = new String[]{"obj"};


    public String[] getSuffixes() {
        return types;
    }


    public IModelCustom loadInstance(ResourceLocation resource) throws WaveFrontObject.ModelFormatException {
        return new WaveFrontObject(resource);
    }
}
