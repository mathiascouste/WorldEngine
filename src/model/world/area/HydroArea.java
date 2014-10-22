package model.world.area;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

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
        this.todayRain = 10;
        this.remainRain = 10;
        this.nuage = 0;
    }

    public void drawHydro(Graphics g) {
        int size = SCALE;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                int transparency = 255;
                if (this.waterLevel[x][y] < 1) {
                    transparency = (int) (255 * this.waterLevel[x][y]);
                }
                g.setColor(new Color(0, 255, 0, transparency));
                g.fillRect(x * size, y * size, size, size);
            }
        }
        System.out.println("Hydro painted");
    }

    private void ecoulement(double[][] nextWaterLevel, int x, int y) {
        int k = this.getAltitude(x, y);
        Point north = new Point(x, y + 1);
        Point south = new Point(x, y - 1);
        Point east = new Point(x + 1, y);
        Point west = new Point(x - 1, y - 1);
        List<Point> pts = new ArrayList<Point>();
        if (getWaterAltitude(north.x, north.y) < k) {
            pts.add(north);
        }
        if (getWaterAltitude(south.x, south.y) < k) {
            pts.add(south);
        }
        if (getWaterAltitude(east.x, east.y) < k) {
            pts.add(east);
        }
        if (getWaterAltitude(west.x, west.y) < k) {
            pts.add(west);
        }
        int maxDiff = 0;
        for (Point p : pts) {
            maxDiff += k - getWaterAltitude(p.x, p.y);
        }
        for (Point p : pts) {
            if (maxDiff != 0 && p.x >= 0 && p.x < SIZE && p.y >= 0
                    && p.y < SIZE) {
                nextWaterLevel[p.x][p.y] += this.waterLevel[x][y] / maxDiff;
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

    private int getWaterAltitude(int x, int y) {
        if (this.isInside(x, y)) {
            if (this.grid != null) {
                int px = (int) (x - this.posX);
                int py = (int) (y - this.posY);
                return (int) (this.relief[px][py] + this.waterLevel[px][py]);
            } else {
                return -1;
            }
        } else {
            this.world.getAltitude(x + this.posX * SIZE, y + this.posY * SIZE);
            return 0;
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

    protected void makeItWet() {
        System.out.println("Remaining Rain = " + this.remainRain);
        if (this.remainRain > 0) {
            double addo = this.todayRain / 50;
            this.remainRain -= addo;
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    this.waterLevel[x][y] += addo;
                }
            }
        }
        System.out.println("Water lvl in 50,50 = " + this.waterLevel[50][50]);
        System.out.println("Remaining Rain = " + this.remainRain);
    }

    protected void thingsWithHydro() {
        System.out.println("HYDROOOO");
        this.makeItWet();

        double[][] nextWaterLevel = new double[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                nextWaterLevel[x][y] = 0;
            }
        }
        for (int k = this.maxHeight; k >= this.minHeight; k--) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    if (this.relief[x][y] == k) {
                        this.infiltration(x, y);
                        this.evaporation(x, y);
                        this.ecoulement(nextWaterLevel, x, y);
                    }
                }
            }
        }
        this.waterLevel = nextWaterLevel;
    }
}
