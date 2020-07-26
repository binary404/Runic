package binary404.runic.api.rune;

import binary404.runic.api.ritual.RitualEffect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Rune {
    String tag;
    Rune[] components;
    RitualEffect effect;

    public static HashMap<Integer, Rune> mixList = new HashMap<>();
    private static ArrayList<Rune> primals = new ArrayList<>();
    private static ArrayList<Rune> compounds = new ArrayList<>();
    private static LinkedHashMap<String, Rune> runes = new LinkedHashMap<>();

    public Rune(String tag, Rune[] components) {
        if (Rune.runes.containsKey(tag)) {
            throw new IllegalArgumentException(tag + " already registered");
        }
        this.tag = tag;
        this.components = components;
        Rune.runes.put(tag, this);
        if (components != null) {
            int h = (components[0].getTag() + components[1].getTag()).hashCode();
            Rune.mixList.put(h, this);
        }
    }

    public Rune(String tag) {
        this(tag, null);
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(final String tag) {
        this.tag = tag;
    }

    public RitualEffect getEffect() {
        return this.effect;
    }

    public void setEffect(RitualEffect effect) {
        this.effect = effect;
    }

    public Rune[] getComponents() {
        return this.components;
    }

    public void setComponents(Rune[] components) {
        this.components = components;
    }

    public boolean isPrimal() {
        return this.getComponents() == null || this.getComponents().length != 2;
    }

    public static Rune getRune(final String tag) {
        return Rune.runes.get(tag);
    }

    public static ArrayList<Rune> getPrimalRunes() {
        if (Rune.primals.isEmpty()) {
            final Collection<Rune> pa = Rune.runes.values();
            for (final Rune aspect : pa) {
                if (aspect.isPrimal()) {
                    Rune.primals.add(aspect);
                }
            }
        }
        return Rune.primals;
    }

    public static ArrayList<Rune> getCompoundRunes() {
        if (Rune.compounds.isEmpty()) {
            final Collection<Rune> pa = Rune.runes.values();
            for (final Rune aspect : pa) {
                if (!aspect.isPrimal()) {
                    Rune.compounds.add(aspect);
                }
            }
        }
        return Rune.compounds;
    }

}
