package model.world;

import java.util.Random;

public class Area {

	public static final int SIZE = 100;
	private int posX;
	private int posY;
	private int grid[][];

	public Area() {
		this(0, 0);
	}

	public Area(int posX, int posY) {
		this.grid = new int[SIZE][SIZE];
		this.posX = posX;
		this.posY = posY;
		Random rand = new Random();
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				this.grid[x][y] = (int) (rand.nextDouble() * Ground
						.getElementsCount());
			}
		}
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}
	
	/* Retourne l'élément de la grille */
	public int getGround(double x, double y) {
		if(this.isInside(x, y)) {
			if(this.grid != null) {
				int px = (int) (x-this.posX);
				int py = (int) (y-this.posY);
				return this.grid[px][py];
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
	
	public void setGround(int x, int y, int ground) throws Exception{
		if(this.grid != null) {
			if(ground < 0 || ground >= Ground.getElementsCount()) {
			} else {
				this.grid[x][y] = ground;
			}
		} else {
		}
	}

	public boolean isInside(double x, double y) {
		if( x >= this.posX && x < this.posX+Area.SIZE && y >= this.posY && y < this.posY+Area.SIZE ) {
			return true;
		} else {
			return false;
		}
	}
}
