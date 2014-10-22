package model.world.area;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import config.GenConfig;

import view.tools.WELogger;

import model.world.Ground;
import model.world.World;
import model.world.entity.Entity;

public class EntityArea extends HydroArea implements Runnable {
    private List<Entity> entities;
    private boolean lock = false;

    public EntityArea(World world) {
        this(world, 0, 0);
    }

    public EntityArea(World world, int posX, int posY) {
        super(world, posX, posY);
        this.entities = new ArrayList<Entity>();
    }

    public void draw(Graphics g) {
        /*int size = SCALE;
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

                int transp = (int) this.waterLevel[x][y];
                if (transp > 255) {
                    transp = 255;
                }
                g.setColor(new Color(0, 255, 0, transp));
                g.fillRect(x * size, y * size, size, size);
            }
        }*/
        this.drawEntities(g);
        System.out.println("Relief painted");
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
        System.out.println("Drawing entities ...");
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

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            thingsWithHydro();
            try {
                Thread.sleep(GenConfig.AREA_DELAY);
            } catch (InterruptedException e) {
                WELogger.log(e);
            }
        }
    }
}
