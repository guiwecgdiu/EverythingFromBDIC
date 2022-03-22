package model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import view.GameClient;

public class Brick implements Move{

	private static int speed;
	private int x,y;
	private boolean live=true;
	private Boolean player;
	private GameClient gameClient;
	private Polygon p;
	private Random r=new Random();
	private double angle;
	private int oldx;//Original X-axis Position 
	private int oldy;//Original Y-axis Position
	private int score;
	private int type;
	
	public void shape(int type) {
		p=new Polygon();
		if(type==1) {
			p.addPoint(x,y-10);
			p.addPoint(x+10, y-5);
			p.addPoint(x+10, y+5);
			p.addPoint(x, y+10);
			p.addPoint(x-10, y+10);
			p.addPoint(x-10, y-5);
			this.speed=Config.FINAL_BRICK_SPEED;
		}
		if(type==2) {
			p.addPoint(x,y-20);
			p.addPoint(x+20, y-10);
			p.addPoint(x+20, y+10);
			p.addPoint(x, y+20);
			p.addPoint(x-20, y+20);
			p.addPoint(x-20, y-10);
			this.speed=Config.SECONDART_BRICK_SPEED;
		}
		if(type==3) {
			p.addPoint(x,y-40);
			p.addPoint(x+40, y-20);
			p.addPoint(x+40, y+20);
			p.addPoint(x, y+40);
			p.addPoint(x-40, y+40);
			p.addPoint(x-40, y-10);
			this.speed=Config.PRIMARY_BRICK_SPEED;
		}
		
	}
	
	//Constructor
	public Brick(int x,int y,Boolean isplayer,int type) {
		this.x=x;
		this.y=y;
		this.player=isplayer;
		this.angle=r.nextInt(360);
		this.type=type;
		this.score=3*(5-type);
		this.shape(type);
	}
	
	//Constructor
	public Brick(int x,int y,Boolean isplayer,GameClient gameClient,int type) {
		this.x=x;
		this.y=y;
		this.player=isplayer;
		this.gameClient=gameClient;
		this.angle=r.nextInt(360);
		this.type=type;
		this.score=3*(5-type);
		this.shape(type);
	}
	
	public void drawBrick(Graphics g) {
		Graphics2D g2=(Graphics2D) g;
		if(!live) {
			if(!player) {			
				gameClient.bricks.remove(this);
			}
			return;
		}
		g2.setColor(Color.WHITE);
		g2.fillPolygon(p);
		this.move();
	}

	public void move() {
		this.oldx=this.x;
		this.oldy=this.y;
		this.x=(int) (x+speed*Math.sin(angle));
		this.y=(int) (y-speed*Math.cos(angle));
		this.shape(this.type);
		
		if(x>Config.GAME_WIDTH) {
			x=0;
		}
		if(y>Config.GAME_HEIGHT){
			y=0;
		}
		if(x<0) {
			x=Config.GAME_WIDTH;
		}
		if(y<0){
			y=Config.GAME_HEIGHT;
		}
	}
	
	
	public boolean iscollision(ArrayList<Brick> bricks) {
		for(int i=0;i<bricks.size();i++){
			Brick b=bricks.get(i);
			if(this.live && b.islive() && this.getRect().intersects(b.getRect())) {
					this.stay();
					return true;
			}
		}
		return false;
	}

	public void split() {
		gameClient.bricks.add(new Brick(this.x, this.y, false, this.gameClient, this.type-1));
		gameClient.bricks.add(new Brick(this.x, this.y, false, this.gameClient, this.type-1));
	}

	public void stay() {
		this.x=this.oldx;
		this.y=this.oldy;
	}

	public boolean islive() {
		return this.live;
	}
	
	public boolean isPlayer() {
		return this.player;
	}

	public void setlive(boolean b) {
		this.live=b;
	}

	public Rectangle getRect() {
		return this.p.getBounds();
	}
	
	public int getx() {
		return x;
	}
	
	public int getScore() {
		return score;
	}

	@Override
	public void turn() {
	}
}

