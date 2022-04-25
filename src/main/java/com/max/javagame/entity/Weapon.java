package com.max.javagame.entity;


import com.max.javagame.entity.mob.Player;
import com.max.javagame.entity.weapon.projectile.Projectile;
import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;

public abstract class Weapon extends Entity {
	protected String name;
	protected String username;
	protected Projectile projectile;

	protected Sprite sprite;
	protected Sprite dropSprite;
	protected double rateOfFire;
	protected double speed;
	protected double range;
	protected int damage;
	protected double spread;

	
	protected double firingTimer;
	
	protected boolean isDropped = false;
	
	public Weapon(String username, Projectile projectile, Sprite sprite, Sprite dropSprite, String name, double rateOfFire, double speed, double spread, double range, int damage) {
		this.speed = speed;
		this.username = username;
		this.projectile = projectile;
		this.rateOfFire = rateOfFire;
		firingTimer = rateOfFire;
		this.spread = spread;
		this.damage = damage;
		this.range = range;
		this.name = name;
		this.sprite = sprite;
		this.dropSprite = dropSprite;

	}
	public abstract void dropWeapon(int x, int y);
	public abstract void update();

	public abstract boolean canPlayerReachWeapon(Player player);
	
	public abstract void render(Render renderer);
	public abstract void shoot(int x, int y);
	public abstract void pickupWeapon();
	
	public String getName() {
		return name;
	}
	public double getWeaponCooldown() {
		return firingTimer;
	}
	public boolean isWeaponDropped() {
		return isDropped;
	}
}
