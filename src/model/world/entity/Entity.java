package model.world.entity;

import java.awt.Graphics;
import java.util.Random;

import model.world.Area;

public abstract class Entity implements Runnable {
	protected double posX, posY, posZ;
	protected long delay;
	protected Area area;
	protected boolean alive;
	protected Thread thread;
	protected double age;
	protected double lifeExpectancy;
	
	public void born() {
		this.age = 0;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public void die() {
		this.alive = false;
		System.out.println("Oh fuck ! I'm dying :'(");
	}
	
	@Override
	public void run() {
		while(this.alive) {
			this.action();
			
			long sleepTime = (long) (this.delay);
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.age += sleepTime/1000;
		}
		if(this.area != null) {
			this.area.removeEntity(this);
		}
		this.thread.interrupt();
	}

	public void action() {
		
	}

	public double getPoxX() {
		return posX;
	}

	public void setPoxX(double poxX) {
		this.posX = poxX;
	}

	public double getPosY() {
		return posY;
	}

	public void setPosY(double posY) {
		this.posY = posY;
	}

	public double getPosZ() {
		return posZ;
	}

	public void setPosZ(double posZ) {
		this.posZ = posZ;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public double getPosX() {
		return posX;
	}

	public void setPosX(double posX) {
		this.posX = posX;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	public double getAge() {
		return age;
	}

	public void setAge(double age) {
		this.age = age;
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
