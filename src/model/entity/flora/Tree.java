package model.entity.flora;

import java.awt.Graphics;
import java.util.Random;

import view.tools.ImageManager;
import view.tools.WELogger;

import model.entity.Entity;
import model.world.Ground;
import model.world.area.EntityArea;

public class Tree extends Entity {
    private static final double LIFEEXPECTANCY = 100.0;

    public Tree() {
        this(null);
    }

    public Tree(EntityArea area) {
        this(area, 0, 0, 0);
    }

    public Tree(EntityArea area, double posX, double posY, double posZ) {
        this.area = area;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.alive = true;
        this.delay = 1000;
        this.lifeExpectancy = LIFEEXPECTANCY;
    }

    public void action() {
        this.reproducing();
        this.oldAgeDie();
    }

    private void oldAgeDie() {
        double probability;
        if (this.age < this.lifeExpectancy) {
            probability = 0.10 * this.age / this.lifeExpectancy;
        } else {
            probability = 0.10 + (this.age - this.lifeExpectancy)
                    / this.lifeExpectancy;
        }
        if (new Random().nextDouble() < probability) {
            this.die();
        }
    }

    private void reproducing() {
        if (new Random().nextDouble() < 0.98) {
            return;
        }
        EntityArea a;
        double newX;
        double newY;
        do {
            newX = this.posX + new Random().nextDouble() * 10;
            newY = this.posY + new Random().nextDouble() * 10;
            a = this.area.getWorld().getAreaPerPosition(newX, newY);
        } while (a == null);

        int ground = a.getGround(newX, newY);

        if (ground != Ground.EARTH && ground != Ground.GRASS) {
            return;
        }

        WELogger.log("I reproduce, i'm a man now !");

        Tree tree = new Tree(a, newX, newY, 0.0);
        this.area.addEntity(tree);
        tree.born();
    }

    public void draw(Graphics g) {
        g.drawImage(ImageManager.getImage("fauna", "tree"),
                (int) (posX * 10) - 5, (int) (posY * 10) - 10, 10, 10, null);
    }
}
