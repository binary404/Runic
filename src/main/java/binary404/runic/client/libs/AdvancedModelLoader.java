package binary404.runic.client.libs;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.Map;

public class AdvancedModelLoader {

    private static Map<String, IModelCustomLoader> instances = Maps.newHashMap();


    public static void registerModelHandler(IModelCustomLoader modelHandler) {
        for (String suffix : modelHandler.getSuffixes()) {
            instances.put(suffix, modelHandler);
        }
    }


    public static IModelCustom loadModel(ResourceLocation resource) throws IllegalArgumentException, WaveFrontObject.ModelFormatException {
        String name = resource.getPath();
        int i = name.lastIndexOf('.');
        if (i == -1) {
            throw new IllegalArgumentException("The resource name is not valid");
        }
        String suffix = name.substring(i + 1);
        IModelCustomLoader loader = instances.get(suffix);
        if (loader == null) {

            throw new IllegalArgumentException("The resource name is not supported");
        }

        return loader.loadInstance(resource);
    }


    public static Collection<String> getSupportedSuffixes() {
        return instances.keySet();
    }


    static {
        registerModelHandler(new ObjModelLoader());
    }

}
