package model.world.generator;

import java.util.Random;

import config.GenConfig;

import model.world.World;
import model.world.area.EntityArea;

public class WorldGenerator extends ReliefGenerator {
    private World world;
    private Calque relief;
    private long seed;
    private Random rand;
    private Calque water;

    public WorldGenerator(World world) {
        super(GenConfig.FREQUENCE, GenConfig.OCTAVE, GenConfig.PERSISTANCE,
                GenConfig.LISSAGE, world.getWidth() * EntityArea.SIZE);
        this.world = world;
        this.seed = new Random().nextLong();
    }

    public void generateMap() {
        this.generateMap(new Random().nextLong());
    }

    public void generateMap(long seed) {
        this.rand = new Random(this.seed);

        this.relief = generateRelief();
        this.world.applyRelief(this.relief);
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
