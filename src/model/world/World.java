package model.world;

import java.util.ArrayList;
import java.util.List;

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
				this.areas.add(new Area(w * Area.SIZE, h * Area.SIZE));
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

	public List<Area> getAreas() {
		return areas;
	}
	
	public Area getAreaPerPosition(double x, double y) {
		for(Area area : this.areas) {
			if(area.isInside(x,y)) {
				return area;
			}
		}
		return null;
	}
	
	public int getGround(double x, double y) {
		Area area = this.getAreaPerPosition(x,y);
		if(area != null) {
			return area.getGround(x, y);
		} else {
			return -1;
		}
	}
	
	public Area getCentralArea() {
		return this.getAreaPerPosition(this.width*Area.SIZE/2, this.height*Area.SIZE/2);
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

}
