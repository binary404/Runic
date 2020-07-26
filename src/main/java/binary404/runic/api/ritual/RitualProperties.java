package binary404.runic.api.ritual;

public class RitualProperties {

    public int size, potency, stability;
    public double decay;
    public boolean chaotic, grounded;

    public RitualProperties(int size, double decay, int potency, int stability, boolean chaotic, boolean grounded) {
        this.size = size;
        this.decay = decay;
        this.potency = potency;
        this.stability = stability;
        this.chaotic = chaotic;
        this.grounded = grounded;
    }

}
