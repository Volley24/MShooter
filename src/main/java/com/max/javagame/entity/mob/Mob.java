package com.max.javagame.entity.mob;

import com.max.javagame.entity.Entity;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.level.tile.Tile;

public abstract class Mob extends Entity {

	public Sprite sprite;
	protected int healthPoints;
	protected int direction = 0;
	protected boolean walking = false;

	public Mob(int x, int y, Sprite sprite, int healthPoints) {
		super(x, y, sprite);
		this.sprite = sprite;
		this.healthPoints = healthPoints;
	}

	public void move(int xAbsolute, int yAbsolute) {
		if (xAbsolute != 0 && yAbsolute != 0) {
			move(xAbsolute, 0);
			move(0, yAbsolute);
			return;
		}
		if (xAbsolute > 0)
			direction = 1;
		if (xAbsolute < 0)
			direction = 3;
		if (yAbsolute > 0)
			direction = 2;
		if (yAbsolute < 0)
			direction = 0;

		if (!collision(xAbsolute, yAbsolute) && !mobCollision(xAbsolute, yAbsolute)) {
			x += xAbsolute;
			y += yAbsolute;
		}else if(getTileCollidingWith(xAbsolute,yAbsolute).equals(Tile.greyBrickTile)) {
			setHealth(healthPoints-1);
		}
	}

	private boolean collision(double xAbsolute, double yAbsolute) {
		boolean solid = false;
		for (int c = 0; c < 4; c++) {
			double xTile = ((x + xAbsolute) + c % 2 * 15) / 16;
			double yTile = ((y + yAbsolute) + c / 2 * 15) / 16;
			if (level.getTile((int) xTile, (int) yTile).isCollidable()) {
				solid = true;
			}
		}

		return solid;
	}
	private Tile getTileCollidingWith(double xAbsolute, double yAbsolute) {

		for (int c = 0; c < 4; c++) {
			double xTile = ((x + xAbsolute) + c % 2 * 15) / 16;
			double yTile = ((y + yAbsolute) + c / 2 * 15) / 16;
			if (level.getTile((int) xTile, (int) yTile).isCollidable()) {
				return level.getTile((int) xTile, (int) yTile);
			}
		}

		return Tile.voidTile;
	}
	public boolean mobCollision(double xAbsolute, double yAbsolute) {
		boolean solid = false;

		double xPos = x + xAbsolute;
		double yPos = y + yAbsolute;

		double yPos2 = yPos + sprite.heightSize;
		for(Entity entity: level.getEntites()) {
			if(!(entity instanceof Mob)) {
				continue;
			}
			if(entity instanceof NetPlayer) {
				if(((NetPlayer)entity).getUserName().equals(Main.player.getUserName())) {
					continue;
				}
			}else if(entity instanceof Player) {
				if(((Player)entity).getUserName().equals(Main.player.getUserName())) {
					continue;
				}
			}
			
			if ((xPos + sprite.widthSize) > ((Mob)entity).x && (xPos + sprite.widthSize) < ((Mob)entity).x + ((Mob)entity).sprite.widthSize) {
				if ((yPos2 > ((Mob)entity).y && yPos < ((Mob)entity).y + ((Mob)entity).sprite.heightSize)) {
					solid = true;
				}
			}

			if ((xPos + sprite.widthSize) > ((Mob)entity).x && (xPos) < ((Mob)entity).x + ((Mob)entity).sprite.widthSize) {
				if ((yPos2 > ((Mob)entity).y && yPos < ((Mob)entity).y + ((Mob)entity).sprite.heightSize)) {
					solid = true;
				}
			}
		}
		
		return solid;
	}
	public abstract void setHealth(int newHealth);
	public abstract void update();

	public abstract void render(Render renderer);

}
