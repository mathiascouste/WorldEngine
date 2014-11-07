package model.entity;

import java.awt.Graphics;

import view.tools.WELogger;

import measure.ThreadsEfficiency;
import model.world.area.EntityArea;

public abstract class Entity implements Runnable {
    protected double posX, posY, posZ;
    protected long delay;
    protected EntityArea area;
    protected boolean alive;
    protected Thread thread;
    protected double age;
    protected double lifeExpectancy;

    public void born() {
        this.age = 0;
        this.thread = new Thread(this);
        this.thread.start();
    }

    public void die() {
        this.alive = false;
        WELogger.log("Oh fuck ! I'm dying :'(");
    }

    @Override
    public void run() {
        while (this.alive) {
            long start = System.currentTimeMillis();

            this.action();
            
            long elapsedTime = System.currentTimeMillis() - start;
            long nextSleep = ThreadsEfficiency.measureNextSleep(delay, elapsedTime);
            try {
                Thread.sleep(nextSleep);
            } catch (InterruptedException e) {
                WELogger.log(e.getMessage());
            }
            this.age += delay / 1000;
        }
        if (this.area != null) {
            this.area.removeEntity(this);
        }
        this.thread.interrupt();
    }

    public void action() {

    }

    public double getPoxX() {
        return posX;
    }

    public void setPoxX(double poxX) {
        this.posX = poxX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public double getPosZ() {
        return posZ;
    }

    public void setPosZ(double posZ) {
        this.posZ = posZ;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public EntityArea getArea() {
        return area;
    }

    public void setArea(EntityArea area) {
        this.area = area;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public double getAge() {
        return age;
    }

    public void setAge(double age) {
        this.age = age;
    }

    public abstract void draw(Graphics g);
}
