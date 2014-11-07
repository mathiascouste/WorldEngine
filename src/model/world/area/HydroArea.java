package model.world.area;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import config.GenConfig;

import model.world.Ground;
import model.world.World;

public class HydroArea extends ReliefArea {
    protected double[][] waterLevel;
    protected double[][] waterInGround;
    protected double nuage;
    protected double todayRain;
    protected double remainRain;

    public HydroArea(World world, int posX, int posY) {
        super(world, posX, posY);
        this.waterLevel = new double[SIZE][SIZE];
        this.waterInGround = new double[SIZE][SIZE];
        this.todayRain = 1;
        this.remainRain = 1;
        this.nuage = 0;
    }

    public void applySeaLevel() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (this.relief[x][y] <= GenConfig.SEALEVEL) {
                    this.waterLevel[x][y] = GenConfig.SEALEVEL
                            - this.relief[x][y];
                }
            }
        }
    }

    public void drawHydro(Graphics g) {
        int size = SCALE;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int transparency = 255;
                if (this.waterLevel[x][y] < 1) {
                    transparency = (int) (255 * this.waterLevel[x][y]);
                }
                g.setColor(new Color(0, 0, 255, transparency));
                g.fillRect(x * size, y * size, size, size);
                if (this.waterLevel[x][y] < 0) {
                    transparency = (int) (255 * this.waterInGround[x][y] / Ground
                            .maxGroundWater(this.grid[x][y]));
                    g.setColor(new Color(255, 0, 0, transparency));
                    g.fillRect(x * size, y * size, size, size);
                }
            }
        }
    }

    private void ecoulement() {
        double[][] entree = new double[SIZE][SIZE];
        double[][] sortie = new double[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                EcoulementCalculeur calculeur = new EcoulementCalculeur(entree,
                        sortie, x, y);
                calculeur.calculate();
            }
        }
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                this.waterLevel[x][y] += entree[x][y];
                this.waterLevel[x][y] -= sortie[x][y];
            }
        }
    }

    private void evaporation(int x, int y) {
        if (this.waterLevel[x][y] > 0) {
            double max = 0.001;
            double coef = 0;
            if (this.waterLevel[x][y] >= 1) {
                coef = 0.25;
            } else {
                coef = 0.25 + 0.75 * (1 - this.waterLevel[x][y]);
            }
            double evaWater = max * coef;
            if (evaWater > this.waterLevel[x][y]) {
                evaWater = this.waterLevel[x][y] / 2;
            }
            this.waterLevel[x][y] -= evaWater;
            this.nuage += evaWater;
        } else {
            double evaGround = this.waterInGround[x][y] / 20;
            this.waterInGround[x][y] -= evaGround;
            this.nuage += evaGround;
        }
    }

    public double getAtmosphereWater() {
        return this.nuage + this.remainRain * SIZE * SIZE;
    }

    public double getNuage() {
        return this.nuage;
    }

    public double getUndergroundWater() {
        double undergroundWater = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                undergroundWater += this.waterInGround[x][y];
            }
        }
        return undergroundWater;
    }

    public double getUppergroundWater() {
        double uppergroundWater = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                uppergroundWater += this.waterLevel[x][y];
            }
        }
        return uppergroundWater;
    }

    private double getWaterAltitude(int x, int y) {
        if (x < 0 || x >= SIZE || y < 0 || y >= SIZE) {
            return GenConfig.MAX_HEIGHT;
        } else {
            return this.relief[x][y] + this.waterLevel[x][y];
        }
    }

    private void infiltration(int x, int y) {
        double maxGroundWater = Ground.maxGroundWater(this.grid[x][y]);
        double groundWaterDiff = maxGroundWater - waterInGround[x][y];
        if (groundWaterDiff > 0) {
            double ajout = groundWaterDiff / 10;
            if (ajout > this.waterLevel[x][y]) {
                ajout = this.waterLevel[x][y] / 2;
            }
            this.waterLevel[x][y] -= ajout;
            this.waterInGround[x][y] += ajout;
        }
    }

    private void rain() {
        if (this.remainRain > 0) {
            double addo = this.todayRain / 50;
            this.remainRain -= addo;
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    this.waterLevel[x][y] += addo;
                }
            }
        }
    }

    public void rainCloud(double coef) throws HydroException {
        if (coef > 1) {
            throw new HydroException("coef out of bound, must be <= 1");
        }
        this.remainRain += coef * nuage;
        this.todayRain += coef * nuage;
        this.nuage -= this.nuage * coef;
    }

    protected void thingsWithHydro() {
        this.rain();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                this.infiltration(x, y);
                this.evaporation(x, y);
            }
        }
        this.ecoulement();
    }

    private class EcoulementCalculeur {
        private double[][] entree, sortie;
        private int x, y;
        private Point north, south, east, west;
        private List<Point> sides;

        public EcoulementCalculeur(double[][] entree, double[][] sortie, int x,
                int y) {
            this.entree = entree;
            this.sortie = sortie;
            this.x = x;
            this.y = y;
            this.north = new Point(x + 1, y);
            this.south = new Point(x - 1, y);
            this.east = new Point(x, y + 1);
            this.west = new Point(x, y - 1);
            this.sides = new ArrayList<Point>();
        }

        private double getPente(Point p) {
            return getWaterAltitude(x, y) - getWaterAltitude(p.x, p.y);
        }

        private void fillSides() {
            if (isInArea(north.x, north.y) && getPente(north) > 0) {
                this.sides.add(north);
            }
            if (isInArea(south.x, south.y) && getPente(south) > 0) {
                this.sides.add(south);
            }
            if (isInArea(east.x, east.y) && getPente(east) > 0) {
                this.sides.add(east);
            }
            if (isInArea(west.x, west.y) && getPente(west) > 0) {
                this.sides.add(west);
            }
        }

        private boolean isInArea(int x, int y) {
            if (x < 0) {
                return false;
            }
            if (y < 0) {
                return false;
            }
            if (x >= SIZE) {
                return false;
            }
            if (y >= SIZE) {
                return false;
            }
            return true;
        }

        public void calculate() {
            this.fillSides();
            double maxOut = 0;
            for (Point p : this.sides) {
                maxOut += calculateOut(p);
            }
            double coef = 1;
            if (maxOut > waterLevel[x][y]) {
                coef = waterLevel[x][y] / maxOut;
            }
            for (Point p : this.sides) {
                entree[p.x][p.y] += calculateOut(p) * coef;
                sortie[x][y] += calculateOut(p) * coef;
            }
        }

        private double calculateOut(Point p) {
            double pente = getPente(p);
            return pente / 50;
        }
    }

    public class HydroException extends Exception {
        private static final long serialVersionUID = 1L;

        public HydroException(String string) {
            super(string);
        }
    }
}
