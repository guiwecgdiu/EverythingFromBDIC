package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import model.Brick;
import model.Config;
import model.MyRandom;

public class Menu extends JPanel {

	private static final long serialVersionUID = 0000000000L;

	private int x = 80;
	private int y = 600;
	private int angle = 0;
	MyRandom r = new MyRandom();
	private String name;
	private GameClient gc;
	public ArrayList<Brick> bricks = new ArrayList<Brick>();

	public Menu(GameClient gc) {
		this.gc = gc;
		this.launchFrame();
	}

	public void paint(Graphics g) {
		super.paint(g);
		for (int i = 0; i < bricks.size(); i++) {
			Brick s = bricks.get(i);
			s.drawBrick(g);
		}
		g.setColor(Color.WHITE);
		Font font = new Font("Arial", Font.BOLD, 40);
		g.setFont(font);
		g.drawString("   SpaceWar" + "\n", 370, 400);
		g.drawString("Enter to start\n", 360, 440);
		g.drawString("", 100, 480);
		font = new Font("Arial", Font.BOLD, 20);
		g.setFont(font);
		g.setColor(Color.magenta);
		g.drawString("SpaceWar is a game which you experience driving your craft to explore between meteorites", 45,
				520);
		g.setColor(Color.YELLOW);
		g.drawString("ABOUT", 15, 560);
		g.setColor(Color.ORANGE);
		g.drawString("Developer", 15, 580);
		g.setColor(Color.RED);
		g.drawString("Arno Azir Leif Kevin Shivani", 15, 600);
		g.setColor(Color.ORANGE);
		g.drawString("Vision", 15, 620);
		g.setColor(Color.RED);
		g.drawString("V10.2", 15, 640);
	}

	public void threadPart() {
		this.repaint();
	}

	public void launchFrame() {
		for (int i = 0; i < 10; i++) {
			switch (r.nextInt(3)) {
			case 0:
				bricks.add(new Brick(0, r.nextInt(Config.GAME_HEIGHT), false, 2));
				break;
			case 1:
				bricks.add(new Brick(Config.GAME_WIDTH, r.nextInt(Config.GAME_HEIGHT), false, 2));
				break;
			case 2:
				bricks.add(new Brick(r.nextInt(Config.GAME_WIDTH), 0, false, 2));
				break;
			}
		}
 		this.setSize(Config.GAME_WIDTH, Config.GAME_HEIGHT);
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		JOptionPane.showMessageDialog(null, "Welcome");
		this.name = JOptionPane.showInputDialog(null, "What's your name?");
		this.gc.setPlayerName(name);
		new Thread(new PaintThread()).start();
	}

	private class PaintThread implements Runnable {
		@Override
		public void run() {
			while (true) {
				threadPart();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
