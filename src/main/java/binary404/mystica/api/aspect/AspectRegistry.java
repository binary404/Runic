package binary404.mystica.api.aspect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class AspectRegistry {

    private static ArrayList<Aspect> primals = new ArrayList<>();

    public static LinkedHashMap<String, Aspect> aspects = new LinkedHashMap<>();

    public static final Aspect AIR = new Aspect("aer", 16777086, "e", 1);
    public static final Aspect EARTH = new Aspect("terra", 5685248, "2", 1);
    public static final Aspect FIRE = new Aspect("ignis", 16734721, "c", 1);
    public static final Aspect WATER = new Aspect("aqua", 3986684, "3", 1);
    public static final Aspect ORDER = new Aspect("ordo", 14013676, "7", 1);
    public static final Aspect CHAOS = new Aspect("perditio", 4210752, "8", 771);

    public static ArrayList<Aspect> getPrimalAspects() {
        if (primals.isEmpty()) {
            Collection<Aspect> pa = aspects.values();
            for (Aspect aspect : pa) {
                if (aspect.isPrimal()) primals.add(aspect);
            }
        }
        return primals;
    }

    private static ArrayList<Aspect> compounds = new ArrayList<>();

    public static ArrayList<Aspect> getCompoundAspects() {
        if (compounds.isEmpty()) {
            Collection<Aspect> pa = aspects.values();
            for (Aspect aspect : pa) {
                if (!aspect.isPrimal()) compounds.add(aspect);
            }
        }
        return compounds;
    }

    public static Aspect getAspect(String tag) {
        return aspects.get(tag);
    }

}
