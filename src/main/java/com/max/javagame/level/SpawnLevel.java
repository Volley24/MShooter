package com.max.javagame.level;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.max.javagame.graphics.SpriteSheet;

public class SpawnLevel extends Level{

	public SpawnLevel(String path) {
		super(path);
	}
	
	protected void loadLevel(String path){
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			this.width = image.getWidth();//Setting the Width and Height of the com.max.javagame.level to the image's width, before, it was equal to 0
			this.height = image.getHeight();

			tiles = new int[width*height];
			image.getRGB(0, 0, width, height, tiles, 0, width);//Array INdex out of bounds at 0??
		} catch (IOException e) {
			System.out.println("Failed to load com.max.javagame.level file.");
			e.printStackTrace();
		}
	}
	
	protected void generateLevel() {

	}
}
