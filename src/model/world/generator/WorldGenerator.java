package model.world.generator;

import java.util.Random;

import model.world.Area;
import model.world.Ground;
import model.world.World;
import model.world.entity.flora.Tree;

public class WorldGenerator {
    private World world;
    private Calque relief;
    private long seed;
    private Random rand;
    private Calque water;

    public WorldGenerator(World world) {
        this.world = world;
    }

    public void generateMap() {
        this.seed = new Random().nextLong();
        this.rand = new Random(this.seed);

        int size = this.world.getWidth();
        this.relief = new ReliefGenerator(5, 4, 0.5f, 4, size).generateRelief();
        this.water = new HydroGenerator(this.relief, rand).generateHydro();

        this.generateEntity(rand);
    }

    private void generateEntity(Random rand) {
        this.generateFlora(rand);
    }

    private void generateFlora(Random rand) {
        for (Area a : this.world.getAreas()) {
            generateTree(rand, a);
        }
    }

    private void generateTree(Random rand, Area a) {
        int maxTree = 100;
        int posX = a.getPosX(), posY = a.getPosY();
        int cptOkGround = 0;
        for (int x = posX; x < Area.SIZE + posX; x++) {
            for (int y = posY; y < Area.SIZE + posY; y++) {
                int ground = a.getGround(x, y);
                if (ground == Ground.EARTH || ground == Ground.GRASS) {
                    cptOkGround++;
                }
            }
        }
        double probaTree = ((double) maxTree) / ((double) cptOkGround);
        for (int x = posX; x < Area.SIZE + posX; x++) {
            for (int y = posY; y < Area.SIZE + posY; y++) {
                int ground = a.getGround(x, y);
                if ((ground == Ground.EARTH || ground == Ground.GRASS)
                        && rand.nextFloat() <= probaTree) {
                    Tree tree = new Tree(a, x + rand.nextDouble(), y
                            + rand.nextDouble(), 0);
                    a.addEntity(tree);
                }
            }
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Calque getRelief() {
        return relief;
    }

    public void setRelief(Calque relief) {
        this.relief = relief;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Calque getWater() {
        return water;
    }

    public void setWater(Calque water) {
        this.water = water;
    }
}
