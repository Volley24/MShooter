package com.max.javagame.level.tile;

import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public class WaterTile extends Tile{

	public WaterTile(Sprite sprite) {
		super(sprite);

	}
	public void render(int x, int y, Render screen) {
		screen.renderTile(x<<4, y<<4, this);
	}

}
