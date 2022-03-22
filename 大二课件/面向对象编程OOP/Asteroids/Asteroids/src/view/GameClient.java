package view;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Brick;
import model.Bullet;
import model.Enemy;
import model.MyRandom;
import model.ScoreBoard;
import model.Score;
import model.Ship;
import model.Config;



public class GameClient extends JPanel {
	
	private static final long serialVersionUID = 0000000000L;

	private double enemyTimer=0;

	private int x = 80 ;
	private int y = 600;
	private int angle=0;
	MyRandom r=new MyRandom();
	private int score;
	private Thread t;
	private String playerName;
	private ScoreBoard sb;
	public Ship myShip=new Ship(x, y,angle,true,this);
	
	public ArrayList<Bullet> clip=new ArrayList<Bullet>();
	public ArrayList<Enemy> BadShips=new ArrayList<Enemy>();
	public ArrayList<Brick> bricks=new ArrayList<Brick>();
	public int state=0;

	public GameClient() throws IOException{
		this.launchFrame();
		sb=new ScoreBoard();
	}
	
	public void setState(int t){
		this.state=t;
	}
	
	public int getState() {
		return this.state;
	}
	
	public void addscore(int m) {
		score=score+m;
	}
	
	public Thread getThread() {
		return t;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(!myShip.iscollision(bricks)) {
			myShip.drawShip(g);
		}
		for (int i = 0; i < clip.size(); i++) { 
			Bullet m = clip.get(i);
			m.hitBrick(bricks);
			m.isHit(myShip);
			m.hitShips(BadShips);
			m.drawBullet(g);
		}
		for(int i=0;i<bricks.size();i++) {
			Brick s=bricks.get(i);
			s.drawBrick(g);
		}
		for(int i=0;i<BadShips.size();i++) {
			Enemy e=BadShips.get(i);
			e.drawShip(g);
		}
		this.enemyTimer=enemyTimer+1;
		if(this.enemyTimer>400) {
			this.BadShips.add(new Enemy(0,r.nextInt(Config.GAME_HEIGHT),this));
			this.enemyTimer=0;
		}
		
		g.setColor(Color.WHITE);
		Font font = new Font("Arial", Font.BOLD, 18);
		g.setFont(font);
		g.drawString("Score:"+score,100,100);
		g.drawString("Life:" +myShip.getlife(),100,120);
		g.drawString("EnemyTimer:"+enemyTimer,100,140);
		if(myShip.getlife()==0) {
			Font f=new Font("Arial", Font.BOLD, 100);
			g.setFont(f);
			g.drawString("Game over", 200, 500);

			Font n=new Font("Arial", Font.BOLD, 18);
			g.setFont(n);
			g.drawString("Enter S into scoreboard", 200, 600);
			if(this.state==0) {
				this.sb.addScore(new Score(playerName,score));
				this.sb.saveScore();
				this.setState(1);
			}
		 }
	}

	public void threadPart() {
		this.repaint();
	}
	
	public void launchFrame() {
		for(int i = 0; i < 10; i++) {
			switch(r.nextInt(3)) {
			case 0:
				bricks.add(new Brick(0,r.nextInt(Config.GAME_HEIGHT),false,this,3));
				break;
			case 1:
				bricks.add(new Brick(Config.GAME_WIDTH,r.nextInt(Config.GAME_HEIGHT),false,this,3));
				break;
			case 2:
				bricks.add(new Brick(r.nextInt(Config.GAME_WIDTH),0,false,this,3));
				break;
			}
		}
		
		this.setSize(Config.GAME_WIDTH,Config.GAME_HEIGHT);
		this.setBackground(Color.BLACK);
		this.setVisible(false);
		t=new Thread(new PaintThread());
		t.start();
		
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}



	public ScoreBoard getSb() {
		return sb;
	}

	public void setSb(ScoreBoard sb) {
		this.sb = sb;
	}

	private class PaintThread implements Runnable{
		@Override
		public void run() {
			while(true) {
				threadPart();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace(); 
				}
			}
		}
	}
	
	public void isHittable() {
		this.myShip.setHittable(true);
	}
	
	public void NotHittable() {
		this.myShip.setHittable(false);
	}
}
