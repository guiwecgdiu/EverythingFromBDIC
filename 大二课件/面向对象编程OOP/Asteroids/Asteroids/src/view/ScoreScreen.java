package view;

import java.awt.*;
import java.io.IOException;

import javax.swing.JPanel;

import model.Config;
import model.ScoreBoard;
import model.Score;

public class ScoreScreen extends JPanel {

    private static final long serialVersionUID = 0000000000L;
    private ScoreBoard board;

    public ScoreScreen(ScoreBoard sb) throws IOException {
        this.board = sb;
        this.launchFrame();
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Config.GAME_WIDTH, Config.GAME_WIDTH);
        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.setColor(Color.GREEN);

        g.drawString("Space War Hall of Fame", 10, 50);
        Score[] scores = board.getScore();
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < scores.length; i++) {
            Score score = scores[i];
            g.drawString(score.getName(), 2 * Config.GAME_WIDTH / 6, 100 + i * 32);
            g.drawString("" + score.getScore(), 4 * Config.GAME_WIDTH / 6, 100 + i * 32);
        }
        g.drawString("Press 'M' to return to the Main Menu", 50, 480);
    }

    public void launchFrame() throws IOException {
        this.board.loadScore();
        this.board.sortScore();
        this.setSize(Config.GAME_WIDTH, Config.GAME_HEIGHT);
        this.setBackground(Color.BLACK);
        this.setVisible(false);

    }

}