package com.max.javagame.entity.weapon.projectile;

import java.util.Random;

import com.max.javagame.entity.Entity;
import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;
public abstract class Projectile extends Entity {
	
	protected final double xOrigin, yOrigin; // Projectile's initial spawn location.
	protected double x, y;
	protected double newX, newY;
	
	protected double angle;
	protected Sprite sprite;
	protected String username;

	protected double distance;
	protected double speed, range;
	public double damage;
	protected double s, spread;
	
	protected final Random random = new Random();

	
	public Projectile(String username, double x, double y, double direction, double speed, double range, int damage, double spread) {
		xOrigin = x;
		yOrigin = y;
		this.x = x;
		this.y = y;
		this.username = username;
		this.spread = spread* (Math.PI / 180);
		s = (random.nextDouble() * this.spread) - (this.spread / 2);
		
		this.range = range;
		this.speed = speed;
		this.damage = damage;
		angle = direction;
	}
	public abstract void update();
	public abstract void render(Render renderer);
	
	public double distance() {
		double aSquared, bSquared, cSquared;
		double distance;
		aSquared = (xOrigin-x)*(xOrigin-x);
		bSquared = (yOrigin-y)*(yOrigin-y);
		cSquared = aSquared + bSquared;
		
		distance = Math.sqrt(cSquared);
		return distance;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	public int getSpriteWidth() {
		return sprite.widthSize;
	}
	public int getSpriteHeight() {
		return sprite.heightSize;
	}
}
