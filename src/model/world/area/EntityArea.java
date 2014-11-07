package model.world.area;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import config.GenConfig;

import view.tools.WELogger;

import measure.ThreadsEfficiency;
import model.entity.Entity;
import model.world.World;

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

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        while (true) {
            long start = System.currentTimeMillis();
            
            thingsWithHydro();
            
            
            long elapsedTime = System.currentTimeMillis() - start;
            long nextSleep = ThreadsEfficiency.measureNextSleep(GenConfig.AREA_DELAY, elapsedTime);
            try {
                Thread.sleep(nextSleep);
            } catch (InterruptedException e) {
                WELogger.log(e);
            }
        }
    }
}
