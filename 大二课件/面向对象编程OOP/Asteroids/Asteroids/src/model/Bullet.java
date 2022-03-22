package model;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import view.GameClient;

public class Bullet implements Move{
	private GameClient gameClient;

	private int x,y;
	private boolean live=true;
	private boolean player;
	private double angle;
	

	public Bullet(int x,int y,double angle){
		 this.x=x;
		 this.y=y;
		 this.angle=angle;
	 }

	public Bullet(int x,int y,boolean player,double angle,GameClient tk) {
		this(x, y, angle);
		this.gameClient=tk;
		this.player=player;
	}
	

	 public void drawBullet(Graphics g) {
		 if(!live) {
			 gameClient.clip.remove(this);
			 return;
		 }
		 g.setColor(Color.ORANGE);
		 g.fillOval(x, y, Config.BULLET_WIDTH, Config.BULLET_HEIGHT);
		 this.move();
	 }

	 @Override
	public void move() {
		 x=(int) (x+Config.BULLET_SPEED*Math.sin(angle));
		 y=(int) (y-Config.BULLET_SPEED*Math.cos(angle));
		
		 if (x < 0 || y < 0 || x > Config.GAME_WIDTH || y > Config.GAME_HEIGHT) {
			this.live = false;
			this.gameClient.clip.remove(this);
		}
	 }

	 @Override
	 public void turn() {
	 }

	 public Rectangle getRect(){
		 return new Rectangle(x-Config.BULLET_WIDTH,y-Config.BULLET_HEIGHT,Config.BULLET_WIDTH,Config.BULLET_HEIGHT);
	 }
	 
	 public boolean islive() {
		 return live;
	 }


	 public boolean isHit(Enemy s) {
		if(this.live && s.islive() && this.getRect().intersects(s.getRect())&&this.player!=s.isPlayer()) {
			s.setlive(false);
			this.live=false;
			this.gameClient.addscore(s.getScore());
			return true;
		}
		return false;
	}
	 
	 public boolean isHit(Ship s) {
			if(this.live && s.islive() && this.getRect().intersects(s.getRect())&&this.player!=s.isPlayer()) {
				if(s.getlife()!=1) {
					this.live=false;
					s.changelife(-1);
				}else {
					this.live=false;
					s.setlive(false);
					s.changelife(-1);
				}
				return true;
			}
			return false;
		}

	 public boolean hitShips(ArrayList<Enemy> ships) {
		 for(int i=0;i<ships.size();i++) {
			 Enemy s=ships.get(i);
			 	if(isHit(s)){
			 		return true;
			 	}
		 }
		 return false;
	 }

	 public boolean isHit(Brick s) {
		 if(this.live && s.islive() && this.getRect().intersects(s.getRect())&&this.player!=s.isPlayer()) {
			 s.setlive(false);
			 s.split();
			 this.live=false;
			 gameClient.addscore(s.getScore());
			 return true;
		 }
		 return false;
	 }
	 
	 public boolean hitBrick(ArrayList<Brick> bricks) {
		 for(int i=0;i<bricks.size();i++) {
			 Brick s=bricks.get(i);
			 if(isHit(s)){
				 return true;
			 }
		 }
	 return false;
	 }
}
