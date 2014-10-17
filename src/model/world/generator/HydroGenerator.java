package model.world.generator;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.GenConfig;

public class HydroGenerator {
    private Calque relief;
    private Calque hydro;
    private Random rand;
    private int taille;
    private List<Point> sources;

    public HydroGenerator(Calque relief, Random rand) {
        this.rand = rand;
        this.relief = relief;
        this.taille = this.relief.getTaille();
        this.hydro = new Calque(this.taille, 0);
        this.sources = new ArrayList<Point>();
    }
    
    public Calque generateHydro() {
        this.placeOcean();
        this.placeSources();
        
        this.startFlow();
        
        return this.hydro;
    }

    private void startFlow() {
        for(Point p : this.sources) {
            this.hydro.getV()[p.x][p.y] = this.relief.getV()[p.x][p.y];
        }
    }

    private void placeOcean() {
        for(int i = 0 ; i < this.taille ; i++) {
            for(int j = 0 ; j < this.taille ; j++) {
                if(this.relief.getV()[i][j] <= GenConfig.SEALEVEL ) {
                    this.hydro.getV()[i][j] = GenConfig.SEALEVEL;
                } else {
                    this.hydro.getV()[i][j] = -1;
                }
            }
        }
    }

    private void placeSources() {
        int nSource = GenConfig.SOURCEABONDANCE;
        int x, y;
        while(nSource!=0) {
            x = rand.nextInt(this.taille);
            y = rand.nextInt(this.taille);
            if(this.hydro.getV()[x][y] == -1) {
                nSource--;
                this.sources.add(new Point(x,y));
            }
        }
    }
}
