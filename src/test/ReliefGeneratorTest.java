package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import config.GenConfig;

import model.world.generator.Calque;
import model.world.generator.HydroGenerator;
import model.world.generator.ReliefGenerator;

public class ReliefGeneratorTest extends JFrame {
    private static final long serialVersionUID = 1L;

    private static int a = 10;
    private static int size = 100;

    public ReliefGeneratorTest(int[][] reliefLvl, int[][] hydroLvl) {
        this.setSize(size * a, size * a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new RelGenPanel(reliefLvl, hydroLvl));

        this.setVisible(true);
    }

    public class RelGenPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private int[][] reliefLvl;
        private int[][] hydroLvl;
        private int tabSize;

        public RelGenPanel(int[][] reliefLvl, int[][] hydroLvl) {
            this.reliefLvl = reliefLvl;
            this.hydroLvl = hydroLvl;
            this.tabSize = reliefLvl.length;
            this.setSize(size * a, size * a);
        }

        public void paint(Graphics g) {
            for (int i = 0; i < this.tabSize; i++) {
                for (int j = 0; j < this.tabSize; j++) {
                    if (this.reliefLvl[i][j] < 0) {
                        this.reliefLvl[i][j] = 0;
                    } else if (this.reliefLvl[i][j] > GenConfig.MAX_HEIGHT) {
                        this.reliefLvl[i][j] = GenConfig.MAX_HEIGHT;
                    }
                    if (this.hydroLvl[i][j] > -1) {
                        int lvl = 255 * this.hydroLvl[i][j]
                                / GenConfig.MAX_HEIGHT;
                        if (this.hydroLvl[i][j] <= GenConfig.SEALEVEL) {
                            g.setColor(new Color(0, lvl, lvl));
                        } else {
                            g.setColor(new Color(0, 0, lvl));
                        }
                    } else {
                        int lvl = 255 * this.reliefLvl[i][j]
                                / GenConfig.MAX_HEIGHT;
                        g.setColor(new Color(lvl, lvl, lvl));
                    }
                    g.fillRect(i * a, j * a, a, a);
                }
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        ReliefGenerator rG = new ReliefGenerator(GenConfig.FREQUENCE,
                GenConfig.OCTAVE, GenConfig.PERSISTANCE, GenConfig.LISSAGE,
                size);

        
        Calque calque = rG.generateRelief();

        HydroGenerator hG = new HydroGenerator(calque, new Random());
        Calque hydro = hG.generateHydro();

        int[][] reliefLvl = calque.getV();
        int[][] hydroLvl = hydro.getV();

        new ReliefGeneratorTest(reliefLvl, hydroLvl);

    }

}
