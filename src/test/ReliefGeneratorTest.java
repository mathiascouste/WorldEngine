package test;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.world.generator.Calque;
import model.world.generator.HydroGenerator;
import model.world.generator.ReliefGenerator;

public class ReliefGeneratorTest extends JFrame {
    private static final long serialVersionUID = 1L;

    private static int a = 10;
    private static int size = 100;

    public ReliefGeneratorTest(int[][] v) {
        this.setSize(size * a, size * a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new RelGenPanel(v));

        this.setVisible(true);
    }

    public class RelGenPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        private int[][] v;

        public RelGenPanel(int[][] v) {
            this.v = v;
            this.setSize(size * a, size * a);
        }

        public void paint(Graphics g) {
            for (int i = 0; i < v.length; i++) {
                for (int j = 0; j < v.length; j++) {
                    if (v[i][j] < 0) {
                        continue;
                    }
                    if (v[i][j] > 255) {
                        v[i][j] = 255;
                    }
                    g.setColor(new Color(0, 0, v[i][j]));
                    if (v[i][j] > 80) {
                        g.setColor(Color.red);
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

        ReliefGenerator rG = new ReliefGenerator(5, 4, 0.5f, 4, size);

        Calque calque = rG.generateRelief();

        HydroGenerator hG = new HydroGenerator(calque, new Random());
        Calque hydro = hG.generateHydro();

        int[][] lvl = calque.getV();
        lvl = hydro.getV();

        new ReliefGeneratorTest(lvl);

    }

}
