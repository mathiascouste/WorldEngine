package model.world.area;

import model.world.World;

public class Area {

    public static final int SIZE = 100;
    public static final int SCALE = 10;

    protected int posX;
    protected int posY;
    protected World world;

    public Area(World world, int posX, int posY) {
        this.world = world;
        this.posX = posX;
        this.posY = posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public static int getScale() {
        return SCALE;
    }

    public static int getSize() {
        return SIZE;
    }

    public World getWorld() {
        return world;
    }

    public boolean isInside(double x, double y) {
        return x >= this.posX && x < this.posX + EntityArea.SIZE
                && y >= this.posY && y < this.posY + EntityArea.SIZE;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
