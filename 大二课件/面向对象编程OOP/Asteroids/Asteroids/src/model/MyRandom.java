package model;

import java.util.Random;

public class MyRandom extends Random {

	private static final long serialVersionUID = 0000000000L;

	int rAngl;

	public int rA0to180() {
		return super.nextInt(180);
	}

	public int rA0to360() {
		return super.nextInt(360);
	}
}
