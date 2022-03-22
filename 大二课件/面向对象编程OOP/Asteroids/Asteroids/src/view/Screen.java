package view;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import control.Controller;
import model.Config;

public class Screen extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameClient gameClient;
	private Menu menu;
	private ScoreScreen scorescreen;
	static JFrame launch = new JFrame("GameClient");
	

	public Screen() throws IOException {
		gameClient = new GameClient();
		scorescreen = new ScoreScreen(gameClient.getSb());
		menu = new Menu(gameClient);
	}

	
	public GameClient getGc() {
		return gameClient;
	}

	public Menu getM() {
		return menu;
	}

	public JFrame getF() {
		return launch;
	}

	public ScoreScreen getS() {
		return scorescreen;
	}

	public void switchs(int state) {
		switch (state) {
		case 1:
			this.getS().setVisible(false);
			this.getGc().setVisible(false);
			this.getM().setVisible(true);
			this.getGc().NotHittable();
			break;
		case 2:
			this.getS().setVisible(false);
			this.getGc().setVisible(true);
			this.getM().setVisible(false);
			this.getGc().isHittable();
			break;
		case 3:
			this.getS().setVisible(true);
			this.getGc().setVisible(false);
			this.getM().setVisible(false);
			this.getGc().NotHittable();
			break;
		}
	}

	public void launchFrame() throws IOException {
		launch.setLocation(400, 0);
		launch.setSize(Config.GAME_WIDTH,Config.GAME_HEIGHT);
		Controller pc = new Controller(this);
		launch.addKeyListener(pc);
		launch.add(this.getGc());
		launch.add(this.getM());
		launch.add(this.getS());
		launch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		launch.setResizable(false);
		launch.setVisible(true);
	}

	
}
