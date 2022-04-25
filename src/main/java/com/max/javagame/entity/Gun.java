package com.max.javagame.entity;

import com.max.javagame.entity.weapon.projectile.Projectile;
import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public abstract class Gun extends Weapon{
	protected int reloadTime;
	protected int magazineSize;
	public double reloadTimer;
	public boolean isReloading = false;
	
	public int ammoLoaded;
	
	public Gun(String username, Projectile projectile, Sprite sprite,Sprite dropSprite, String name, int reloadTime, int magazineSize, double rateOfFire,double speed, double spread, double range, int damage) {
		//Projectile projectile, Sprite sprite, Sprite dropSprite, String name, double rateOfFire, double speed, double range, double spread, int damage
		super(username, projectile, sprite,dropSprite, name, rateOfFire, speed, spread,range, damage);
		this.firingTimer = rateOfFire;
		this.reloadTimer = reloadTime;
		this.reloadTime = reloadTime;
		this.magazineSize = magazineSize;
	}

	public abstract void render(Render renderer);
	public abstract void update();
	public abstract void shoot(int x, int y);
	public abstract void reload();
	public abstract void cancelReload();


}
