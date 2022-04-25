package com.max.javagame.entity;

import com.max.javagame.entity.mob.Player;
import com.max.javagame.entity.weapon.projectile.Bullet;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.input.Mouse;
import com.max.javagame.graphics.Sprite;

public class AssaultRifle extends Gun {

	public AssaultRifle(String username) {
		//int reloadTime, int magazineSize, double rateOfFire,double speed, double spread, double range, int damage
		super(username,null, Sprite.bulletSprite, Sprite.assaultRifleDrop, "Assault Rifle", (2*60)+30, 30, 6, 7, 15, 300, 10);

	}

	@Override
	public void render(Render renderer) {
		if(isDropped) {
			renderer.renderSprite((int)x, (int)y, dropSprite);
		}

	}

	@Override
	public void update() {
		if(isReloading) {
			if(!(reloadTimer <= 0)) {
				reloadTimer--;
			}else if(reloadTimer <= 0){
				this.ammoLoaded = magazineSize;
				reloadTimer = reloadTime; 
				isReloading = false;
			}
		}
		if(!(firingTimer <= 0)) {
			firingTimer--;
		}else if(firingTimer <= 0) {
			firingTimer = 0;
		}

	}

	@Override
	public void shoot(int x, int y) {
		if (firingTimer <= 0 && this.ammoLoaded >= 1 && !isReloading) {
			double deltaX = Mouse.getX() - (Main.getWindowWidth() / 2) + 30;// +8
			double deltaY = Mouse.getY() - (Main.getWindowHeight() / 2) + 30;// +8
			double angle = Math.atan2(deltaY, deltaX);// atan2 = y first then x

			//double x, double y, double direction, double speed, double spread, double range, int damage
			projectile = new Bullet(username,x, y, angle,speed,spread,range,damage);
			this.ammoLoaded--;
			firingTimer = rateOfFire;
			reloadTimer = reloadTime;
			level.add(projectile);

		}

	}

	@Override
	public void reload() {
		if(ammoLoaded != magazineSize) {
			isReloading = true;
		}
	}

	@Override
	public void cancelReload() {
		isReloading = false;

	}

	@Override
	public void dropWeapon(int x, int y) {
		this.x =x;
		this.y =y;
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
