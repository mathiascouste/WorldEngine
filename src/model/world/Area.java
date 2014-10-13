package model.world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import view.tools.WELogger;

import model.world.entity.Entity;

public class Area {

    public static final int SIZE = 100;
    public static final int SCALE = 10;
    private int posX;
    private int posY;
    private int[][] grid;
    private List<Entity> entities;
    private World world;

    public Area(World world) {
        this(world, 0, 0);
    }

    public Area(World world, int posX, int posY) {
        this.world = world;
        this.entities = new ArrayList<Entity>();
        this.grid = new int[SIZE][SIZE];
        this.posX = posX;
        this.posY = posY;
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
                int ground = this.grid[x][y];
                switch (ground) {
                case Ground.EARTH:
                    g.setColor(Color.ORANGE);
                    break;
                case Ground.GRASS:
                    g.setColor(Color.GREEN);
                    break;
                case Ground.ROCK:
                    g.setColor(Color.GRAY);
                    break;
                case Ground.SAND:
                    g.setColor(Color.YELLOW);
                    break;
                case Ground.WATER:
                    g.setColor(Color.BLUE);
                    break;
                default:
                    g.setColor(Color.GRAY);
                }
                g.fillRect(x * size, y * size, size, size);
            }
        }
        this.drawEntities(g);
    }

    private boolean lock = false;

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
}
