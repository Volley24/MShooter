package com.max.javagame.level;

import java.util.Random;

public class RandomLevel extends Level{
	private static final Random random = new Random();
	public RandomLevel(int width, int height) {
		super(width, height);//Calls constructor from super class (Map)

	}
	protected void generateLevel() {
		for(int y = 0; y<height;y++) {
			for(int x = 0; x<width;x++) {
				int odds = random.nextInt(10);//1 in 10 for a stone
				switch (odds) {
				case 0:
					tilesInt[x+y*width] = 2;	//1 in 10 chance for stone to appear
					break;
				case 1:
					tilesInt[x+y*width] = 1;
					break;
				case 2:
					tilesInt[x+y*width] = 0;
					break;
				case 3:
					tilesInt[x+y*width] = 0;
					break;
				case 4:
					tilesInt[x+y*width] = 0;
					break;
				case 5:
					tilesInt[x+y*width] = 0;
					break;
				case 6:
					tilesInt[x+y*width] = 0;
					break;
				case 7:
					tilesInt[x+y*width] = 0;
					break;
				case 8:
					tilesInt[x+y*width] = 0;
					break;
				case 9:
					tilesInt[x+y*width] = 0;
					break;
				}
			}
		}
	}
}
