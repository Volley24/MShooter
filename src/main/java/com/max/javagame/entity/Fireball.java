package com.max.javagame.entity;

import com.max.javagame.entity.mob.Player;
import com.max.javagame.entity.weapon.projectile.FireballProjectile;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.input.Mouse;

public class Fireball extends Weapon{
	public static final int MANA_COST = 50;
	
	public Fireball(String username) {
		super(username,null, Sprite.fireballSprite, Sprite.fireballSprite, "Fireball", 60,4.5, 100, 20,40);
		
	}
	

	@Override
	public void render(Render renderer) {
		if(isDropped) {
			renderer.renderSprite((int)x, (int)y, dropSprite);
		}
	}

	@Override
	public void shoot(int x, int y) {
		if (firingTimer <= 0) {
			double deltaX = Mouse.getX() - (Main.getWindowWidth() / 2) + 30;// +8
			double deltaY = Mouse.getY() - (Main.getWindowHeight() / 2) + 30;// +8
			double angle = Math.atan2(deltaY, deltaX);// atan2 = y first then x
			
			projectile = new FireballProjectile(username, x, y, angle,speed,spread, range, damage);
			firingTimer = rateOfFire;
			level.add(projectile);

		}
	}
	public int getManaCost() {
		return MANA_COST;
	}

	@Override
	public void update() {

		if(!(firingTimer <= 0)) {
			firingTimer--;
		}else if(firingTimer <= 0) {
			firingTimer = 0;
		}
		
	}


	@Override
	public void dropWeapon(int x, int y) {
		this.x =x;
		this.y = y;
		isDropped = true;
		
	}


	@Override
	public boolean canPlayerReachWeapon(Player player) {
		if(!isDropped) {
			return false;
		}
	
		double playerX = player.x;
		double playerY = player.y;
		
		double playerWidth = player.sprite.widthSize;
		double playerHeight = player.sprite.heightSize;
		
		double dropX = x;
		double dropY = y;
		
		double dropWidth = Sprite.machineGunDrop.widthSize;
		double dropHeight = Sprite.machineGunDrop.heightSize;
		
		double centerX = playerX + (playerWidth/2);
		double centerY = playerY + (playerHeight/2);
		
		
		if(centerX > dropX && centerX < dropX+dropWidth  &&  centerY > dropY && centerY < dropY+dropHeight) {
			return true;
		}
		return false;
	}


	@Override
	public void pickupWeapon() {
		isDropped = false;
		
	}


}
