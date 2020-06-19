package binary404.runic.client.libs;

import net.minecraft.util.ResourceLocation;

public interface IModelCustomLoader {
    String getType();

    String[] getSuffixes();

    IModelCustom loadInstance(ResourceLocation paramResourceLocation) throws WaveFrontObject.ModelFormatException;
}
