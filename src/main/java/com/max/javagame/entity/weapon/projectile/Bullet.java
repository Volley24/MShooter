package com.max.javagame.entity.weapon.projectile;

import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public class Bullet extends Projectile {
	//protected String username;
	public Bullet(String username, double x, double y, double direction, double speed, double spread, double range, int damage) {
		//double x, double y, double direction, double speed, double range, int damage, double spread
		super(username, x, y, direction, speed, range, damage, spread);
		sprite =  Sprite.bulletSprite;
		newX = speed * Math.cos(angle+s);// Determine how much we move on the X when there is an angle
		newY = speed * Math.sin(angle+s);
	}

	@Override
	public void update() {
		this.x += newX;
		this.y += newY;
		if(level.tileCollision(x, y, newX, newY,4)) {
			
			remove();
		}
		if(level.mobCollisionWithProjectile(x, y, newX, newY,4,this,username)) {
			remove();
		}
		if(distance() > range) {
			remove();
		}
	}
	@Override
	public void render(Render renderer) {
		renderer.renderProjectile((int)x, (int)y, this);
	}

}
