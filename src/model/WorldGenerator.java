package model;

import java.util.Random;

import model.world.Area;
import model.world.Ground;
import model.world.World;

public class WorldGenerator {
	private World world;
	public WorldGenerator(World world) {
		this.world = world;
	}
	
	public void generateMap() {
		this.generateMap(new Random().nextLong());
	}
	private void generateMap(long seed) {
		Random rand = new Random(seed);
		this.generateWater();
		this.generateEarth(rand);
		this.generateGrassAndSand(rand);
	}

	private void generateWater() {
		int lWidth = world.getWidth()*Area.SIZE;
		int lHeight = world.getHeight()*Area.SIZE;
		for(int x = 0 ; x < lWidth ; x++) {
			for(int y = 0 ; y < lHeight  ; y++) {
				world.setGround(x, y, Ground.WATER);
			}
		}
	}

	private void generateEarth(Random rand) {
		int lWidth = world.getWidth()*Area.SIZE;
		int lHeight = world.getHeight()*Area.SIZE;
		int nIsland = 30;
		int islandMaxRadius = 20;
		for(int i = 0 ; i < nIsland ; i++) {
			int xC = (int) (rand.nextDouble()*lWidth);
			int yC = (int) (rand.nextDouble()*lHeight);
			int radius = (int) (rand.nextDouble()*islandMaxRadius);
			for(int x = xC-radius ; x < xC + radius ; x++) {
				if(x >= 0 && x < lWidth) {
					for(int y = yC-radius ; y < yC + radius ; y++) {
						if(y >= 0 && y < lHeight && distance(x,y,xC,yC) <= radius) {
							world.setGround(x,y,Ground.EARTH);
						}
					}
				}
			}
		}
	}

	private double distance(int xA, int yA, int xB, int yB) {
		return Math.sqrt(Math.pow(xB-xA,2)+Math.pow(yB-yA,2));
	}

	private void generateGrassAndSand(Random rand) {
		int lWidth = world.getWidth()*Area.SIZE;
		int lHeight = world.getHeight()*Area.SIZE;
		for(int x = 0 ; x < lWidth ; x++) {
			for(int y = 0 ; y < lHeight  ; y++) {
				if(world.getGround(x, y) == Ground.EARTH) {
					double arridity = world.getArridity(x,y)+rand.nextDouble()*0.3-0.15;
					if(arridity > 0.95) {
						world.setGround(x, y, Ground.SAND);
					} else if(arridity < 0.8) {
						world.setGround(x, y, Ground.GRASS);
					}
				}
			}
		}
	}
}
