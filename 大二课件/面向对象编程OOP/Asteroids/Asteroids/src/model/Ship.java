package model;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import view.GameClient;

public class Ship implements Move {
	private GameClient gameClient;
	private int x, y;
	private double angle;
	private boolean player;
	private boolean live = true;
	private Operation dir;
	private boolean bu = false, br = false, bl = false, bd = false;
	private Polygon p;
	private int oldx; 
	private int oldy;
	private double oldangle;
	private int speedx = 0;
	private int speedy = 0;
	private int life = 100;
	private boolean Hittable;

	// Constructor
	public Ship() {
	}
	
	// Constructor
	public Ship(int x, int y, double angle, GameClient m) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.gameClient = m;
		this.shape();
		this.Hittable=true;
	}

	public Ship(int x, int y, double angle, boolean good, GameClient m) {
		this(x, y, angle, m);
		this.player = good;
		this.oldx = x;
		this.oldy = y;
		this.shape();
		this.Hittable=true;
	}

	public void shape() {
		p = new Polygon();
		p.addPoint(x, y - 25);
		p.addPoint(x - 44, y + 50);
		p.addPoint(x, y + 65);
		p.addPoint(x + 44, y + 50);
	}

	public void rotateToAngle(int[] x, int[] y, double theta, int x0, int y0) {
		for (int i = 0; i < x.length; i++) {
			int tempx = x[i];
			x[i] = (int) ((x[i] - x0) * Math.cos(theta) - (y[i] - y0) * Math.sin(theta) + x0);
			y[i] = (int) ((tempx - x0) * Math.sin(theta) + (y[i] - y0) * Math.cos(theta) + y0);
		}
	}

	public void rotateToOffSetAngle() {
		this.shape();
		this.rotateToAngle(p.xpoints, p.ypoints, this.angle, x, y);
	}

	public void setOffsetAngle(double dTheta) {
		this.angle = (this.angle + dTheta) % (2 * Math.PI);
	}

	public void drawShip(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (!live) {
			return;
		}
		if (player) {
			g2.setColor(Color.RED);
		}
		g2.fillPolygon(p);
		this.move();
	}

	public void move() {
		this.oldx = this.x;
		this.oldy = y;
		this.oldangle = this.angle;

		if (this.player) {
			this.x = (this.x + this.speedx);
			this.y = (this.y - this.speedy);
			this.rotateToOffSetAngle();
			if (this.bu) {
				this.speedx = (int) (this.speedx + Config.SHIP_ACCLERATION * Math.sin(this.angle));
				this.speedy = (int) (this.speedy + Config.SHIP_ACCLERATION * Math.cos(this.angle));
				this.rotateToOffSetAngle();
			}

			if (this.br) {
				this.setOffsetAngle(Config.SHIP_Rangular);
				this.rotateToOffSetAngle();
			}
			if (bl) {
				this.setOffsetAngle(-Config.SHIP_Rangular);
				this.rotateToOffSetAngle();
			}
			if (bd) {
				speedx=(int)(speedx - Config.SHIP_ACCLERATION * Math.sin(angle));
				speedy=(int)(speedy - Config.SHIP_ACCLERATION * Math.cos(angle));
				this.rotateToOffSetAngle();
			}

		if (x > Config.GAME_WIDTH) {
			x = 0;
		}
		if (y > Config.GAME_HEIGHT) {
			y = 0;
		}
		if (x < 0) {
			x = Config.GAME_WIDTH;
		}
		if (y < 0) {
			y = Config.GAME_HEIGHT;
		}
	}
	}

	private Bullet fire() {
		if (!live) {
			return null;
		}
		Bullet m = new Bullet(p.xpoints[0], p.ypoints[0], player, this.angle, gameClient);// change
		gameClient.clip.add(m);
		return m;
	}

	// getter and setter
	public Rectangle getRect() {
		return this.p.getBounds();
	}

	public boolean islive() {
		return this.live;
	}

	public int getlife() {
		return this.life;
	}

	public boolean isPlayer() {
		return this.player;
	}

	public void setlive(boolean live) {
		this.live = live;
	}

	public boolean iscollision(ArrayList<Brick> bricks) {
		if(this.Hittable) {
			for (int i = 0; i < bricks.size(); i++) {
				Brick s = bricks.get(i);
				if (this.live && s.islive() && this.getRect().intersects(s.getRect())) {
					this.stay();
					if (this.player != s.isPlayer()) {
						if (this.life != 1) {
							s.setlive(false);
							this.life--;
						} else {
							this.setlive(false);
							s.setlive(false);
							this.life--;
						}
						return true;
					}
				}
			}
			return false;
		}else {
			return false;
		}
	}
	
	public void stay() {
		this.x = this.oldx;
		this.y = this.oldy;
	}

	// controller
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode(); 
		switch (key) {
		case KeyEvent.VK_LEFT:
			bl = true;
			break;
		case KeyEvent.VK_UP:
			bu = true;
			break;
		case KeyEvent.VK_RIGHT:
			br = true;
			break;
		case KeyEvent.VK_DOWN:
			bd = true;
			break;
		}
		this.changeDir();
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch (key) {
		case KeyEvent.VK_SPACE:
			fire();
			break;
		case KeyEvent.VK_LEFT:
			bl = false;
			break;
		case KeyEvent.VK_UP:
			bu = false;
			break;
		case KeyEvent.VK_RIGHT:
			br = false;
			break;
		case KeyEvent.VK_DOWN:
			bd = false;
			break;
		}
		this.changeDir();
	}

	private void changeDir() {
		if (bl && !bu && !br && !bd)
			dir = Operation.LE;
		else if (!bl && bu && !br && !bd)
			dir = Operation.UP;
		else if (!bl && !bu && br && !bd)
			dir = Operation.RI;
		else if (!bl && !bu && !br && bd)
			dir = Operation.DOWN;
		else if (bl && !bu && !br && !bd)
			dir = Operation.LE;
		else if (!bl && !bu && !br && !bd)
			dir = Operation.STOP;
	}

	public void turn() {
	}

	public void changelife(int i) {
		this.life=this.life+i;
	}

	public void setHittable(boolean b) {
		this.Hittable=b;
	}
}
