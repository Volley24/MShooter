package com.max.javagame.level.tile;

import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public class StoneTile extends Tile {

	public StoneTile(Sprite sprite) {
		super(sprite);
	}
	public void render(int x, int y, Render screen) {
		screen.renderTile(x<<4, y<<4, this);
	}
	public boolean isCollidable(){
		return true;
	} 
}
