package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

import model.world.generator.Calque;
import model.world.generator.ReliefGenerator;

public class ReliefGeneratorTest extends JFrame {
    private static int a = 1;
    private static int size = 1000;
    
    
    public ReliefGeneratorTest(int [][] v) {
        this.setSize(size*a,size*a);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.add(new RelGenPanel(v));
        
        this.setVisible(true);
    }
    
    public class RelGenPanel extends JPanel {
        private int[][] v;
        //private int a;
        
        public RelGenPanel(int [][] v) {
            //a = 10;
            
            this.v = v;
            this.setSize(size*a,size*a);
        }
        
        public void paint(Graphics g) {
            for(int i = 0 ; i < v.length ; i++) {
                for(int j = 0 ; j < v.length ; j++) {
                    if(v[i][j]>255) v[i][j] = 255;
                    g.setColor(new Color(v[i][j],v[i][j],v[i][j]));
                    int waterLvl = 120;
                    if(v[i][j]<waterLvl) {
                        g.setColor(new Color(0,0,v[i][j]));
                    }
                    g.fillRect(i*a, j*a, a, a);
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
        
        int[][] lvl = calque.getV();
        /*for(int i = 0 ; i < size ; i++) {
            for(int j = 0 ; j < size ; j++) {
                int l = lvl[i][j];
                if(l < 10) System.out.print(" ");
                if(l < 100) System.out.print(" ");
                System.out.print(l+" ");
            }
            System.out.println("");
        }*/
        
        new ReliefGeneratorTest(lvl);
        
    }

}
