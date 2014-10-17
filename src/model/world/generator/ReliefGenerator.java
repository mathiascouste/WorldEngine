package model.world.generator;

public class ReliefGenerator {
    private int frequence;
    private int octave;
    private float persistance;
    private int taille;
    private int lissage;

    private Calque calque;

    public ReliefGenerator(int frequence, int octave, float persistance,
            int lissage, int taille) {
        this.frequence = frequence;
        this.taille = taille;
        this.octave = octave;
        this.persistance = persistance;
        this.lissage = lissage;
    }

    public Calque generateRelief() {
        this.calque = new Calque(taille, 1);

        Calque.genererCalque(this.frequence, this.octave, this.persistance,
                this.lissage, this.calque);

        return this.calque;
    }
}
