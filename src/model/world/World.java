package model.world;

import java.util.ArrayList;
import java.util.List;

import config.GenConfig;

import view.tools.WELogger;

import model.world.entity.Entity;
import model.world.generator.Calque;
import model.world.generator.WorldGenerator;

public class World {

    private List<Area> areas;
    private int width;
    private int height;

    public World() {
        this(1, 1);
    }

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.areas = new ArrayList<Area>();
        for (int w = 0; w < this.width; w++) {
            for (int h = 0; h < this.height; h++) {
                this.areas.add(new Area(this, w * Area.SIZE, h * Area.SIZE));
            }
        }
        new WorldGenerator(this).generateMap();
    }

    public void start() {
        for (Area a : this.areas) {
            new Thread(a).start();
            a.lock();
            for (Entity e : a.getEntities()) {
                e.born();
            }
            a.unlock();
        }
    }

    public double getArridity(int x, int y) {
        return 1 - getHumidity(x, y);
    }

    public double getHumidity(int x, int y) {
        int radius = 10;
        double arridity = 0;
        for (int i = (int) (x - radius); i < x + radius; i++) {
            for (int j = (int) (y - radius); j < y + radius; j++) {
                if (getGround(i, j) == Ground.WATER) {
                    arridity += 1;
                }
            }
        }
        arridity /= Math.pow(2 * radius, 2);
        return arridity;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public Area getAreaPerPosition(double x, double y) {
        for (Area area : this.areas) {
            if (area.isInside(x, y)) {
                return area;
            }
        }
        return null;
    }

    public int getGround(double x, double y) {
        Area area = this.getAreaPerPosition(x, y);
        if (area != null) {
            return area.getGround(x, y);
        } else {
            return -1;
        }
    }

    public int getAltitude(double x, double y) {
        Area area = this.getAreaPerPosition(x, y);
        if (area != null) {
            return area.getAltitude(x, y);
        } else {
            return 0;
        }
    }

    public void setGround(double x, double y, int ground) {
        Area area = this.getAreaPerPosition(x, y);
        area.setGround(x, y, ground);
    }

    public Area getCentralArea() {
        return this.getAreaPerPosition(this.width * Area.SIZE / 2, this.height
                * Area.SIZE / 2);
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void printInfo() {
        int entityCount = 0;
        for (Area a : this.areas) {
            entityCount += a.getEntities().size();
        }
        WELogger.log(entityCount + " entity(ies) in this world !");
    }

    public void applyRelief(Calque relief) {
        for (Area a : this.areas) {
            int posX = a.getPosX(), posY = a.getPosY();
            int[][] areaRelief = a.getRelief();
            for (int x = posX; x < posX + Area.SIZE; x++) {
                for (int y = posY; y < posY + Area.SIZE; y++) {
                    areaRelief[x - posX][y - posY] = relief.getV()[x][y];
                }
            }
            a.calculateMaxHeight();
            a.calculateMinHeight();
        }
        this.defineGround();
    }

    private void defineGround() {
        for (int x = 0; x < this.width * Area.SIZE; x++) {
            for (int y = 0; y < this.height * Area.SIZE; y++) {
                if (this.getAltitude(x, y) <= GenConfig.SEALEVEL) {
                    this.setGround(x, y, Ground.WATER);
                } else {
                    int alt = getAltitude(x, y);
                    int penteX = 0;
                    int penteY = 0;
                    if (x == 0) {
                        penteX = alt - getAltitude(x + 1, y);
                        penteX *= 2;
                    } else if (x == this.width * Area.SIZE) {
                        penteX = alt - getAltitude(x - 1, y);
                        penteX *= 2;
                    } else {
                        penteX = getAltitude(x + 1, y) - getAltitude(x - 1, y);
                    }
                    if (y == 0) {
                        penteY = alt - getAltitude(x, y + 1);
                        penteY *= 2;
                    } else if (y == this.height * Area.SIZE) {
                        penteX = alt - getAltitude(x, y - 1);
                        penteX *= 2;
                    } else {
                        penteX = getAltitude(x, y + 1) - getAltitude(x, y - 1);
                    }
                    penteX = Math.abs(penteX);
                    penteY = Math.abs(penteY);
                    int pente = (int) (penteX / 2f + penteY / 2f);
                    if (pente < 10) {
                        this.setGround(x, y, Ground.EARTH);
                    } else {
                        this.setGround(x, y, Ground.ROCK);
                    }
                }
            }
        }
    }
}
