package model;

public class Score {
	private String name;
    private int score;

    public Score(String name, int score) {
        this.score = score;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getScore() {
        return this.score;
    }
}
