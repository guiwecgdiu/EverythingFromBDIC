package model;

import java.awt.*;
import java.util.Random;

import view.GameClient;;

public class Enemy {
	private GameClient gameClient;

	enum Direction {UP,DOWN,RI,LE,STOP,UR,UL,DR,DL};
	private Direction dir=Direction.STOP;
	private boolean player;
	private boolean live=true;
	private int score=50;
	
	private Random r=new Random();
	private int stepx=r.nextInt(12)+3;
 
	int x = 50 , y = 50;
	
	int centreX=x+Config.SHIP_WIDTH/2;
	int centreY=y+Config.SHIP_HEIGHT/2;
	
	public Enemy() {}
	public Enemy(int x,int y,GameClient m) {
		this.x=x;
		this.y=y;
		this.gameClient=m;
	}
	
	public void drawShip(Graphics g) {
		if(!live) {
			if(!player) {
				gameClient.BadShips.remove(this);
			}
			return;
		}
		g.setColor(Color.BLUE);
		Polygon p = new Polygon();
		p.addPoint(x, y - 25);
		p.addPoint(x - 44, y + 50);
		p.addPoint(x, y + 65);
		p.addPoint(x + 44, y + 50);
		Graphics2D g2 = (Graphics2D) g;
		g2.fillPolygon(p);
		this.movable();
	}
 
	
	
	public void movable() {
		switch(dir) {
			case UP:
				y=y-Config.SHIP_SPEED;
				break;
			case DOWN:
				y=y+Config.SHIP_SPEED;
				break;
			case RI:
				x=x+Config.SHIP_SPEED;
				break;
			case LE:
				x=x-Config.SHIP_SPEED;
			case STOP:
				break;
			case DL:
				x=x-Config.SHIP_SPEED;
				y=y+Config.SHIP_SPEED;
				break;
			case DR:
				x=x+Config.SHIP_SPEED;
				y=y+Config.SHIP_SPEED;
				break;
			case UR:
				x=x+Config.SHIP_SPEED;
				y=y-Config.SHIP_SPEED;
				break;
			case UL:
				x=x-Config.SHIP_SPEED;
				y=y-Config.SHIP_SPEED;
				break;
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
	
		if (!player) {
			Direction[] dirs = Direction.values();	
			if (stepx == 0) {
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
				stepx = r.nextInt(22) + 3;
			}	
			stepx--;
			
			if(r.nextInt(40)> 37) {
				this.fire();
			}
	}
		
	}
	private Bullet fire() {
		if(!live)return null;
		int x=this.x;
		int y = this.y;
		Bullet m = new Bullet(x, y,this.player,this.angle(), gameClient) ;
		gameClient.clip.add(m);
		return m;
	}
	
	public double angle(){
		double angle=0;
		switch(dir) {
			case UP:
				angle=0;
				break;
			case DOWN:
				angle=180;
				break;
			case RI:
				angle=90;
				break;
			case LE:
				angle=270;
			case STOP:
				angle=90;
				break;
			case DL:
				angle=225;
				break;
			case DR:
				angle=135;
				break;
			case UR:
				angle=45;
				break;
			case UL:
				angle=315;
				break;
			}
		return angle;
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,Config.SHIP_WIDTH,Config.SHIP_HEIGHT);
	}
		
	public boolean islive() {
		return this.live;
	}
	
	public boolean isPlayer() {
		return this.player;
	}
	
	public void setlive(boolean live) {
		this.live=live;
	}
	public int getScore() {
		return score;
	}
}
