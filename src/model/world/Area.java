package model.world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import config.GenConfig;

import view.tools.WELogger;

import model.world.entity.Entity;

public class Area implements Runnable {

    public static final int SIZE = 100;
    public static final int SCALE = 10;
    private int posX;
    private int posY;
    private List<Entity> entities;
    private World world;
    private boolean lock = false;

    private int[][] grid;
    private int[][] relief;
    private int[][] waterLevel;
    private int todayRain;

    private int maxHeight;
    private int minHeight;

    public Area(World world) {
        this(world, 0, 0);
    }

    public Area(World world, int posX, int posY) {
        this.world = world;
        this.entities = new ArrayList<Entity>();
        this.grid = new int[SIZE][SIZE];
        this.relief = new int[SIZE][SIZE];
        this.waterLevel = new int[SIZE][SIZE];
        this.todayRain = 150;
        this.posX = posX;
        this.posY = posY;
        this.maxHeight = 0;
        this.minHeight = GenConfig.MAX_HEIGHT;
        Random rand = new Random();
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                this.grid[x][y] = (int) (rand.nextDouble() * Ground
                        .getElementsCount());
            }
        }
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
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

    public void setGround(double x, double y, int ground) {
        if (this.grid != null && this.isInside(x, y)) {
            int px = (int) (x - this.posX);
            int py = (int) (y - this.posY);
            this.grid[px][py] = ground;
        }
    }

    public boolean isInside(double x, double y) {
        return x >= this.posX && x < this.posX + Area.SIZE && y >= this.posY
                && y < this.posY + Area.SIZE;
    }

    public void draw(Graphics g) {
        int size = SCALE;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                g.setColor(Color.WHITE);
                g.fillRect(x * size, y * size, size, size);

                int transparency = 255 - 120 * this.relief[x][y]
                        / GenConfig.MAX_HEIGHT;
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
                
                int transp = this.waterLevel[x][y];
                if(transp > 255) transp = 255;
                g.setColor(new Color(0, 255, 0, transp));
                g.fillRect(x * size, y * size, size, size);
            }
        }
        this.drawEntities(g);
    }

    public synchronized void lock() {
        if (lock) {
            try {
                wait();
            } catch (InterruptedException e) {
                WELogger.log(e.getMessage());
            }
        }
        this.lock = true;
    }

    public synchronized void unlock() {
        this.lock = false;
        this.notify();
    }

    public synchronized void drawEntities(Graphics g) {
        lock();
        for (Entity e : this.entities) {
            e.draw(g);
        }
        unlock();
    }

    public synchronized void addEntity(Entity entity) {
        lock();
        this.entities.add(entity);
        unlock();
    }

    public synchronized void removeEntity(Entity entity) {
        lock();
        this.entities.remove(entity);
        unlock();
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] grid) {
        this.grid = grid;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public static int getSize() {
        return SIZE;
    }

    public static int getScale() {
        return SCALE;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public int[][] getRelief() {
        return relief;
    }

    public void setRelief(int[][] relief) {
        this.relief = relief;
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

    @Override
    public void run() {
        while (true) {
            makeItWet();
            makeWaterFlow();
            try {
                Thread.sleep(GenConfig.AREA_DELAY);
            } catch (InterruptedException e) {
                WELogger.log(e);
            }
        }
    }

    private void makeItWet() {
        if (this.todayRain > 0) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    this.waterLevel[x][y] += this.todayRain;
                }
            }
        }
        this.todayRain /= 2;
    }

    private void makeWaterFlow() {
        System.out.println("Water is flowing ...");
        int[][] nextWaterLevel = new int[SIZE][SIZE];
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                nextWaterLevel[x][y] = 0;
            }
        }
        for (int k = this.maxHeight; k >= this.minHeight; k--) {
            for (int x = 0; x < SIZE; x++) {
                for (int y = 0; y < SIZE; y++) {
                    if (this.relief[x][y] == k) {
                        Point north = new Point(x, y + 1);
                        Point south = new Point(x, y - 1);
                        Point east = new Point(x + 1, y);
                        Point west = new Point(x - 1, y - 1);
                        List<Point> pts = new ArrayList<Point>();
                        if (getAltitude(north.x, north.y) < k) {
                            pts.add(north);
                        }
                        if (getAltitude(south.x, south.y) < k) {
                            pts.add(south);
                        }
                        if (getAltitude(east.x, east.y) < k) {
                            pts.add(east);
                        }
                        if (getAltitude(west.x, west.y) < k) {
                            pts.add(west);
                        }
                        int maxDiff = 1;
                        for (Point p : pts) {
                            maxDiff += k - getAltitude(p.x, p.y);
                        }
                        for (Point p : pts) {
                            if (p.x >= 0 && p.x < SIZE && p.y >= 0
                                    && p.y < SIZE) {
                                nextWaterLevel[p.x][p.y] += this.waterLevel[x][y]
                                        / maxDiff;
                            }
                        }
                    }
                }
            }
        }
        this.waterLevel = nextWaterLevel;
        System.out.println("Water stopped flowing ...");
    }

    public int[][] getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(int[][] waterLevel) {
        this.waterLevel = waterLevel;
    }
}
