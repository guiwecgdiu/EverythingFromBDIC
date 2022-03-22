package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class ScoreBoard {
    private Score[] scores;
    public ScoreBoard() throws IOException {
        this.scores = new Score[10];
        for (int i = 0; i < 10; i++) {
            scores[i] = new Score("No player yet", 0);
        }
        this.loadScore();
    }

    public void sortScore() {
        Arrays.sort(scores, new Comparator<Score>() {
            public int compare(Score o1, Score o2) {
                return Integer.compare(o2.getScore(), o1.getScore());
            }
        });
    }

    public void loadScore() throws IOException {
        File f1 = new File("ScoreBorad.txt");
        if (!f1.exists()) {
            f1.createNewFile();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f1))) {
            String line = br.readLine();
            int i = 0;
            while (line != null && i < 10) {
                Scanner s = new Scanner(line);
                int score = s.nextInt();
                String name=s.next();
                scores[i] = new Score(name, score);
                i++;
                line = br.readLine();
                s.close();
            }

        } catch (Exception e) {
            System.err.println("Error Reading!");
            e.printStackTrace();
        }
    }

    public Score[] getScore() {
        Score[] scs = new Score[10];
        for (int i = 0; i < scs.length; i++) {
            scs[i] = scores[i];
        }
        return scs;
    }

    public void saveScore() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("ScoreBorad.txt"))) {
            for (int i = 0; i < scores.length; i++) {
                bw.write(scores[i].getScore() + " " + scores[i].getName() + "\n");
            }
            bw.close();
        } catch (Exception e) {
            System.err.println("Error Writing!");
        }
    }

    public void addScore(Score s) {
        sortScore();
        if (scores[9].getScore() < s.getScore()) {
            scores[9] = s;
        }
        sortScore();
    }

}

