package model.world;

import java.util.ArrayList;
import java.util.List;

import model.WorldGenerator;
import model.world.entity.Entity;

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
		// TODO Auto-generated method stub
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
		for(Area a  : this.areas) {
			entityCount += a.getEntities().size();
		}
		System.out.println(entityCount + " entity(ies) in this world !");
	}

}
