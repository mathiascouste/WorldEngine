package model.world.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.GenConfig;

public class Calque {
    private int[][] v;
    private int taille;
    private float persistance;

    public Calque(int taille, float persistance) {
        this.taille = taille;
        this.persistance = persistance;
        this.v = new int[this.taille][this.taille];
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                this.v[i][j] = 0;
            }
        }
    }

    public static Calque initCalque(int taille, float persistance) {
        return new Calque(taille, persistance);
    }

    public void randomize() {
        for (int i = 0; i < this.taille; i++) {
            for (int j = 0; j < this.taille; j++) {
                this.v[i][j] = (int) (new Random().nextDouble() * GenConfig.MAX_HEIGHT);
            }
        }
    }

    public int[][] getV() {
        return v;
    }

    public void setV(int[][] v) {
        this.v = v;
    }

    public int getTaille() {
        return taille;
    }

    public void setTaille(int taille) {
        this.taille = taille;
    }

    public float getPersistance() {
        return persistance;
    }

    public void setPersistance(float persistance) {
        this.persistance = persistance;
    }

    public static void genererCalque(int frequence, int octaves,
            float persistance, int liss, Calque c) {
        // itératif
        int taille = c.taille;
        int i, j, n, fCourante;
        int x = 0, y = 0;
        int a;
        float sumPersistances;
        float persistanceCourante = persistance;

        // calque aléatoire
        Calque random = new Calque(taille, persistance);
        random.randomize();

        // calques de travail
        List<Calque> calques = new ArrayList<Calque>();
        for (i = 0; i < octaves; i++) {
            calques.add(new Calque(taille, persistanceCourante));
            persistanceCourante *= persistance;
        }

        fCourante = frequence;

        // remplissage de calque
        for (n = 0; n < octaves; n++) {
            for (i = 0; i < taille; i++) {
                for (j = 0; j < taille; j++) {
                    a = valeurInterpolee(i, j, fCourante, random);
                    calques.get(n).getV()[i][j] = a;
                }
            }
            fCourante *= frequence;
        }

        sumPersistances = sommePersistance(octaves, calques);

        // ajout des calques successifs
        for (i = 0; i < taille; i++) {
            for (j = 0; j < taille; j++) {
                for (n = 0; n < octaves; n++) {
                    c.getV()[i][j] += calques.get(n).getV()[i][j]
                            * calques.get(n).getPersistance();
                }
                // normalisation
                c.getV()[i][j] = (int) (c.getV()[i][j] / sumPersistances);
            }
        }

        // lissage
        Calque lissage = new Calque(taille, 0);

        for (x = 0; x < taille; x++) {
            for (y = 0; y < taille; y++) {
                lissage(liss, c, taille, x, y, lissage);
            }
        }
    }

    private static float sommePersistance(int octaves, List<Calque> calques) {
        int i;
        float sumPersistances;
        sumPersistances = 0;
        for (i = 0; i < octaves; i++) {
            sumPersistances += calques.get(i).getPersistance();
        }
        return sumPersistances;
    }

    private static int lissage(int liss, Calque c, int taille, int x, int y,
            Calque lissage) {
        int n = 0, a = 0, l = 0;
        int k2 = 0;
        for (k2 = x - liss; k2 <= x + liss; k2++) {
            for (l = y - liss; l <= y + liss; l++) {
                if ((k2 >= 0) && (k2 < taille) && (l >= 0) && (l < taille)) {
                    n++;
                    a += c.getV()[k2][l];
                }
            }
        }
        lissage.getV()[x][y] = a / n;
        return l;
    }

    private static int valeurInterpolee(int i, int j, int frequence, Calque r) {
        // valeurs des bornes
        int borne1x, borne1y, borne2x, borne2y, q;
        float pas = r.getTaille() / frequence;

        q = (int) (((float) i) / pas);
        borne1x = (int) (q * pas);
        borne2x = (int) ((q + 1) * pas);

        if (borne2x >= r.getTaille()) {
            borne2x = r.getTaille() - 1;
        }

        q = (int) (((float) j) / pas);
        borne1y = (int) (q * pas);
        borne2y = (int) ((q + 1) * pas);

        if (borne2y >= r.getTaille()) {
            borne2y = r.getTaille() - 1;
        }
        int b00, b01, b10, b11;
        b00 = r.getV()[borne1x][borne1y];
        b01 = r.getV()[borne1x][borne2y];
        b10 = r.getV()[borne2x][borne1y];
        b11 = r.getV()[borne2x][borne2y];

        int v1 = interpolate(b00, b01, borne2y - borne1y, j - borne1y);
        int v2 = interpolate(b10, b11, borne2y - borne1y, j - borne1y);
        return interpolate(v1, v2, borne2x - borne1x, i - borne1x);
    }

    private static int interpolate(int y1, int y2, int n, int delta) {

        // interpolation non linéaire
        if (n == 0) {
            return y1;
        }
        if (n == 1) {
            return y2;
        }

        float a = (float) delta / n;

        float fac1 = (float) (3 * Math.pow(1 - a, 2) - 2 * Math.pow(1 - a, 3));
        float fac2 = (float) (3 * Math.pow(a, 2) - 2 * Math.pow(a, 3));

        return (int) (y1 * fac1 + y2 * fac2);
    }
}
