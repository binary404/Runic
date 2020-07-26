package binary404.runic.api.rune;

import binary404.runic.api.ritual.RitualEffectLife;

public class Runes {

    public static Rune FIRE = new Rune("fire");
    public static Rune WATER = new Rune("water");
    public static Rune EARTH = new Rune("earth");
    public static Rune AIR = new Rune("air");
    public static Rune CHAOS = new Rune("chaos");
    public static Rune ORDER = new Rune("order");

    public static Rune LIFE = new Rune("life", new Rune[]{WATER, EARTH});

    public static void initEffects() {
        LIFE.setEffect(new RitualEffectLife());
    }

}
