package com.max.javagame.entity.weapon.projectile;

import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public class FireballProjectile extends Projectile {
	protected String username;
	public FireballProjectile(String username, double x, double y, double direction, double speed, double range, double spread, int damage) {
		super(username, x, y, direction, speed, range, damage, spread);
		range = 200;
		sprite = Sprite.fireballSprite;
		newX = speed * Math.cos(angle);// Determine how much we move on the X when there is an angle
		newY = speed * Math.sin(angle);
	}

	public void update() {
		move();
	}

	public void move() {
		this.x += newX;
		this.y += newY;

		if (level.tileCollision(x, y, newX, newY, 8)) {
			remove();
		}

		if (level.mobCollisionWithProjectile(x, y, newX, newY, 8,this,username)) {

			remove();
		}

		if (distance() > range) {

			remove();
		}

	}

	public void render(Render renderer) {
		renderer.renderProjectile((int) x, (int) y, this);
	}
}
