package model.world;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Area {

	public static final int SIZE = 100;
	public static final int SCALE = 10;
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
	
	public void setGround(double x, double y, int ground) {
		if(this.isInside(x, y)) {
			if(this.grid != null) {
				int px = (int) (x-this.posX);
				int py = (int) (y-this.posY);
				this.grid[px][py] = ground;
			}
		}
	}

	public boolean isInside(double x, double y) {
		if( x >= this.posX && x < this.posX+Area.SIZE && y >= this.posY && y < this.posY+Area.SIZE ) {
			return true;
		} else {
			return false;
		}
	}

	public void draw(Graphics g) {
		int size = SCALE;
		for(int x = 0 ; x < SIZE ; x++) {
			for(int y = 0 ; y < SIZE ; y++) {
				int ground = this.grid[x][y];
				switch(ground) {
				case Ground.EARTH:
					g.setColor(Color.ORANGE);
					break;
				case Ground.GRASS:
					g.setColor(Color.GREEN);
					break;
				case Ground.ROCK:
					g.setColor(Color.GRAY);
					break;
				case Ground.SAND:
					g.setColor(Color.YELLOW);
					break;
				case Ground.WATER:
					g.setColor(Color.BLUE);
					break;
				}
				g.fillRect(x*size, y*size, size, size);
			}
		}
	}
}
