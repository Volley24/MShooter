package com.max.javagame.level.tile;

import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public class Tile {
	public int x, y;
	public Sprite sprite;
	
	public static Tile grassTile = new GrassTile(Sprite.grassSprite);
	public static Tile stoneTile = new StoneTile(Sprite.stoneSprite);
	public static Tile redBrickTile = new RedBrick(Sprite.redBrickSprite);
	public static Tile greyBrickTile = new GreyBrick(Sprite.greyBrickSprite);
	public static Tile waterTile = new WaterTile(Sprite.waterSprite);
	public static Tile voidTile = new VoidTile(Sprite.voidSprite);
	public Tile(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void render(int x, int y, Render screen) {
		
	}
	public boolean isCollidable(){
		return false;
	}
}
