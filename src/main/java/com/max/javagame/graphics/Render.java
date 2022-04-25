package com.max.javagame.graphics;

import java.util.Random;

import com.max.javagame.entity.weapon.projectile.Projectile;
import com.max.javagame.level.tile.Tile;


public class Render{
	private final static int MAP_SIZE = 64;
	private Random random = new Random();
	
	public int width;
	public int height;
	public int[] pixels;
	public int tiles[] = new int[MAP_SIZE*MAP_SIZE];//Map of Tiles of 64 by 64 tiles
	
	public int xOffset, yOffset;



	public Render(int width, int height) {
		
		this.width = width;
		this.height = height;
		pixels = new int[width * height];//50624
		//Sprites will need to be called here INSTEAD of the Sprite class:
		for(int i =0; i< tiles.length;i++) {
			tiles[i] = random.nextInt(0xffffff);
			tiles[0] = 0;
		}
	}
	public void clear() {
		for(int i = 0; i < pixels.length; i++) {
			pixels[i] = 0;
		}
	}
	public void renderTile(int xPos, int yPos, Tile tile) {
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y<tile.sprite.heightSize; y++){
			int yAbsolute = y+yPos;
			
			for(int x = 0; x<tile.sprite.widthSize; x++){
				int xAbsolute = x+xPos;
				
				if(xAbsolute < -tile.sprite.widthSize || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height) break;
				if(xAbsolute<0) xAbsolute= 0;
				pixels[xAbsolute+yAbsolute*width] = tile.sprite.pixels[x+y*tile.sprite.widthSize];
			}
		}
	}

	public void renderProjectile(int xPos, int yPos, Projectile projectile) {
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y<projectile.getSpriteHeight(); y++){
			int yAbsolute = y+yPos;
			
			for(int x = 0; x<projectile.getSpriteWidth(); x++){
				int xAbsolute = x+xPos;
				
				if(xAbsolute < -projectile.getSpriteWidth() || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height) break;
				if(xAbsolute<0) xAbsolute= 0;
				
				int color = projectile.getSprite().pixels[x+y*projectile.getSprite().widthSize];
				if(color != 0xffff00ff) 
					pixels[xAbsolute+yAbsolute*width] = color;
			}
		}
	}
	public void renderSprite(int xPos, int yPos, Sprite sprite) {
		xPos -= xOffset;
		yPos -= yOffset;
		for(int y = 0; y<sprite.heightSize; y++){
			int yAbsolute = y+yPos;
			for(int x = 0; x<sprite.widthSize; x++){
				int xAbsolute = x+xPos;

				if(xAbsolute < -sprite.widthSize || xAbsolute >= width || yAbsolute < 0 || yAbsolute >= height) break;
				if(xAbsolute<0) xAbsolute= 0;
				int color = sprite.pixels[x+y*sprite.widthSize];
				
				if(color != 0xffff00ff) 
				pixels[xAbsolute+yAbsolute*width] = color;
			}
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}
}
