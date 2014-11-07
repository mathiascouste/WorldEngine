package model.world.area;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import config.GenConfig;
import model.world.Ground;
import model.world.World;

public class ReliefArea extends Area {

    protected int[][] grid;
    protected int[][] relief;

    protected int maxHeight;
    protected int minHeight;

    public ReliefArea(World world, int posX, int posY) {

        super(world, posX, posY);
        this.relief = new int[SIZE][SIZE];
        this.maxHeight = 0;
        this.grid = new int[SIZE][SIZE];
        this.minHeight = GenConfig.MAX_HEIGHT;
        Random rand = new Random();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                this.grid[x][y] = (int) (rand.nextDouble() * Ground
                        .getElementsCount());
            }
        }
    }

    public void calculateMaxHeight() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (this.relief[x][y] > this.maxHeight) {
                    this.maxHeight = this.relief[x][y];
                }
            }
        }
    }

    public void calculateMinHeight() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if (this.relief[x][y] < this.minHeight) {
                    this.minHeight = this.relief[x][y];
                }
            }
        }
    }

    public int getAltitude(double x, double y) {
        if (this.isInside(x, y)) {
            if (this.grid != null) {
                int px = (int) (x - this.posX);
                int py = (int) (y - this.posY);
                return this.relief[px][py];
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGround(double x, double y, int ground) {
        if (this.grid != null && this.isInside(x, y)) {
            int px = (int) (x - this.posX);
            int py = (int) (y - this.posY);
            this.grid[px][py] = ground;
        }
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    /* Retourne l'élément de la grille */
    public int getGround(double x, double y) {
        if (this.isInside(x, y)) {
            if (this.grid != null) {
                int px = (int) (x - this.posX);
                int py = (int) (y - this.posY);
                return this.grid[px][py];
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public int[][] getRelief() {
        return relief;
    }

    public void setRelief(int[][] relief) {
        this.relief = relief;
    }
    
    public void drawRelief(Graphics g) {
        int size = SCALE;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.setColor(Color.black);
                g.fillRect(x * size, y * size, size, size);

                int transparency = 255 * this.relief[x][y]
                        / GenConfig.MAX_HEIGHT+255*(this.maxHeight/GenConfig.MAX_HEIGHT);
                int ground = this.grid[x][y];
                switch (ground) {
                case Ground.EARTH:
                    g.setColor(new Color(200, 180, 90, transparency));
                    break;
                case Ground.GRASS:
                    g.setColor(new Color(0, 255, 0, transparency));
                    break;
                case Ground.ROCK:
                    g.setColor(new Color(255 / 3, 255 / 3, 255 / 3,
                            transparency));
                    break;
                case Ground.SAND:
                    g.setColor(Color.YELLOW);
                    break;
                case Ground.WATER:
                    g.setColor(new Color(0, 0, 255, transparency));
                    break;
                default:
                    g.setColor(Color.GRAY);
                }
                g.fillRect(x * size, y * size, size, size);
            }
        }
    }
}
