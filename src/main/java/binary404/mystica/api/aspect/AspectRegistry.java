package binary404.mystica.api.aspect;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;

public class AspectRegistry {

    private static ArrayList<Aspect> primals = new ArrayList<>();

    public static LinkedHashMap<String, Aspect> aspects = new LinkedHashMap<>();

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
