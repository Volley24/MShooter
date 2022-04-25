package com.max.javagame.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SpriteSheet {
	private String path;
	public final int size;
	public int pixels[];

	public static SpriteSheet tilesSprite = new SpriteSheet("/spritesheets/tile_spritesheet.png", 256);
	public static SpriteSheet playerSprite = new SpriteSheet("/spritesheets/mob_spritesheet.png", 256);
	public static SpriteSheet projectileBox = new SpriteSheet("/spritesheets/projectiles/projectile_spritesheet.png", 48);
	public static SpriteSheet weaponDrops = new SpriteSheet("/spritesheets/weapons/weapon_drops.png", 64);
	public static SpriteSheet inventory = new SpriteSheet("/spritesheets/inventory.png", 64);


	
	public SpriteSheet(String path, int size) {
		this.path = path;
		this.size = size;
		pixels = new int[size*size];//65536
		load();
	}
	private void load() {
		try {
			BufferedImage image = ImageIO.read(SpriteSheet.class.getResource(path));
			int width = image.getWidth();
			int height = image.getHeight();
			image.getRGB(0, 0,width,height,pixels, 0,width);//transform image into array of pixels giving RBG values
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
