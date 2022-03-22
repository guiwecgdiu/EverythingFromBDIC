package control;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import view.Screen;

public class Controller implements KeyListener{
	
	private Screen screen;//variable:contolled screen
	
	//method:constructor
	public Controller() {}
	
	//method:constructor
	public Controller(Screen s) {
		this.screen=s;	
	}

	@Override
	public void keyPressed(KeyEvent e) {
		screen.getGc().myShip.keyPressed(e);
		int key=e.getKeyCode();
		if(key==KeyEvent.VK_ENTER) {
			screen.switchs(2);
			screen.getF().repaint();
		}	
		if(key==KeyEvent.VK_M) {
			screen.switchs(1);
			screen.getF().repaint();
		}
		if(key==KeyEvent.VK_S) {
			if (screen.getGc().getState() == 1) {
				screen.switchs(3);
				screen.getF().repaint();
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		screen.getGc().myShip.keyReleased(e);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
}

