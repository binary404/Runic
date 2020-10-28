package binary404.mystica.api.aspect;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Aspect {

    String tag;
    Aspect[] components;
    int color;
    private String chatcolor;
    ResourceLocation image;
    int blend;
    public static HashMap<Integer, Aspect> mixList = new HashMap<>();


    public Aspect(String tag, int color, Aspect[] components, ResourceLocation image, int blend) {
        if (AspectRegistry.aspects.containsKey(tag)) throw new IllegalArgumentException(tag + " already registered!");
        this.tag = tag;
        this.components = components;
        this.color = color;
        this.image = image;
        this.blend = blend;
        AspectRegistry.aspects.put(tag, this);
        if (components != null) {
            int h = (components[0].getTag() + components[1].getTag()).hashCode();
            mixList.put(Integer.valueOf(h), this);
        }
    }


    public Aspect(String tag, int color, Aspect[] components) {
        this(tag, color, components, new ResourceLocation("mystica", "textures/aspects/" + tag.toLowerCase() + ".png"), 1);
    }


    public Aspect(String tag, int color, Aspect[] components, int blend) {
        this(tag, color, components, new ResourceLocation("mystica", "textures/aspects/" + tag.toLowerCase() + ".png"), blend);
    }


    public Aspect(String tag, int color, String chatcolor, int blend) {
        this(tag, color, (Aspect[]) null, blend);
        setChatcolor(chatcolor);
    }


    public int getColor() {
        return this.color;
    }


    public String getName() {
        return WordUtils.capitalizeFully(this.tag);
    }


    public String getLocalizedDescription() {
        return I18n.format("mystica.aspect." + this.tag);
    }


    public String getTag() {
        return this.tag;
    }


    public void setTag(String tag) {
        this.tag = tag;
    }


    public Aspect[] getComponents() {
        return this.components;
    }


    public void setComponents(Aspect[] components) {
        this.components = components;
    }


    public ResourceLocation getImage() {
        return this.image;
    }


    public int getBlend() {
        return this.blend;
    }


    public void setBlend(int blend) {
        this.blend = blend;
    }


    public boolean isPrimal() {
        return (getComponents() == null || (getComponents()).length != 2);
    }

    public String getChatcolor() {
        return this.chatcolor;
    }


    public void setChatcolor(String chatcolor) {
        this.chatcolor = chatcolor;
    }


}
