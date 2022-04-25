package com.max.javagame.entity;

import java.util.Random;

import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.level.Level;

public class Entity {
	public double x, y;
	public boolean isRemoved = false;
	protected Level level;
	protected Sprite sprite;
	protected final Random random = new Random();
	
	public Entity() {
		
	}
	public Entity(int x, int y, Sprite sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	public void init(Level level) {
		this.level = level;
	}
	public void update() {
		
	}
	
	public void render(Render renderer) {
		if(sprite != null) {
			renderer.renderSprite((int)x, (int)y, sprite);
		}
	}
	
	public void remove(){
		//Remove com.max.javagame.entity from com.max.javagame.level/map.
		isRemoved = true;
	}
	public boolean isRemoved() {
		return isRemoved;
	}
}
