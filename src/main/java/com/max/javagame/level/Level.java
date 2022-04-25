package com.max.javagame.level;

import java.util.ArrayList;
import java.util.List;

import com.max.javagame.entity.Entity;
import com.max.javagame.entity.Weapon;
import com.max.javagame.entity.mob.Mob;
import com.max.javagame.entity.mob.NetPlayer;
import com.max.javagame.entity.mob.Player;
import com.max.javagame.entity.weapon.projectile.Projectile;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.level.tile.Tile;
import com.max.javagame.net.packets.Packet03DamagePlayer;

public class Level {
	protected int width, height;
	protected int tilesInt[];
	protected int tiles[];

	private List<Entity> entites = new ArrayList<Entity>();
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	private List<Weapon> weapons = new ArrayList<Weapon>();

	public static Level lakeVilleMap = new SpawnLevel("/maps/first_map.png");

	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		tilesInt = new int[width * height];
		generateLevel();

	}

	public Level(String path) {
		loadLevel(path);
		generateLevel();
	}

	protected void generateLevel() {
		// Generate a random map using tiles:

	}

	protected void loadLevel(String path) {
		// Load a Map from a path:
	}

	public void update() {

		for (int i = 0; i < entites.size(); i++) {

			entites.get(i).update();
		}
		for (int i = 0; i < weapons.size(); i++) {
			weapons.get(i).update();
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).update();
		}
	}

	public boolean tileCollision(double x, double y, double xAbsolute, double yAbsolute, int size) {
		// Collision between tiles and projectiles!
		// X and Y will be pos of com.max.javagame.entity
		boolean solid = false;

		for (int c = 0; c < 4; c++) {
			int xTile = (((int) x + (int) xAbsolute) + c % 2 * size / 2 + 5) / 16;
			int yTile = (((int) y + (int) yAbsolute) + c / 2 * size + 3) / 16;
			if (getTile(xTile, yTile).isCollidable()) {
				solid = true;
			}
		}

		return solid;
	}

	public void render(int xScroll, int yScroll, Render screen) {
		// >> 4 == /16
		screen.setOffset(xScroll, yScroll);
		int x0 = xScroll >> 4, y0 = yScroll >> 4;
		int x1 = (xScroll + screen.width + 16) >> 4, y1 = (yScroll + screen.height + 16) >> 4;
		for (int y = y0; y < y1; y++) {
			for (int x = x0; x < x1; x++) {
				getTile(x, y).render(x, y, screen);

			}
		}
		for (int i = 0; i < entites.size(); i++) {
			Entity e = entites.get(i);
			if (e.isRemoved()) {
				entites.remove(i);
			}
		}

		for (int i = 0; i < weapons.size(); i++) {
			if (weapons.get(i).isWeaponDropped()) {
				weapons.get(i).render(screen);
			}
		}
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).render(screen);
		}
		for (int i = 0; i < entites.size(); i++) {
			entites.get(i).render(screen);

		}
	}

	public void generateWeapon(int x, int y, Weapon weapon) {
		if (weapon != null) {
			if (!(getTile(x, y).isCollidable())) {
				this.add(weapon);
				weapon.dropWeapon(x, y);
			} else {
				System.out.println("Could not spawn weapon as loacation is inside a solid block");
			}

		}
	}

	public boolean mobCollisionWithProjectile(double x, double y, double xAbsolute, double yAbsolute, int size,
			Projectile projectile, String username) {

		double xPos = x + xAbsolute;
		double yPos = y + yAbsolute;

		double yPos2 = yPos + size;
		for (Entity entity : getEntites()) {
			if (!(entity instanceof Player)) {
				continue;
			} 
			if (((Player) entity).getUserName().trim().equalsIgnoreCase(username)) {
				continue;
			}
			
			if ((xPos + size) > ((Mob) entity).x
					&& (xPos + size) < ((Mob) entity).x + ((Mob) entity).sprite.widthSize) {
				if ((yPos2 > ((Mob) entity).y && yPos < ((Mob) entity).y + ((Mob) entity).sprite.heightSize)) {

					((Player) entity).setHealth(((Player) entity).getHealthPoints() - (int) projectile.damage);
					System.out.println("Hit " + ((Player) entity).getUserName() +" doing "+(int) projectile.damage+" damage.");
					Packet03DamagePlayer damagePlayerWhoIsShot = new Packet03DamagePlayer(
							((Player) entity).getUserName(), (int) projectile.damage);
					if (Main.main.gameClient != null) {
						damagePlayerWhoIsShot.writeData(Main.main.gameClient);
					} else if (Main.main.gameServer != null) {
						damagePlayerWhoIsShot.writeData(Main.main.gameServer);
					}
					return true;

				}
			}

			if ((xPos + size) > ((Mob) entity).x
					&& (xPos + size) < ((Mob) entity).x + ((Mob) entity).sprite.widthSize) {
				if ((yPos2 > ((Mob) entity).y && yPos < ((Mob) entity).y + ((Mob) entity).sprite.heightSize)) {

					((Player) entity).setHealth(((Player) entity).getHealthPoints() - (int) projectile.damage);

					System.out.println("Hit " + ((Player) entity).getUserName() +" doing "+(int) projectile.damage+" damage.");

					Packet03DamagePlayer damagePlayerWhoIsShot = new Packet03DamagePlayer(
							((Player) entity).getUserName(), (int) projectile.damage);
					if (Main.main.gameClient != null) {
						damagePlayerWhoIsShot.writeData(Main.main.gameClient);
					} else if (Main.main.gameServer != null) {
						damagePlayerWhoIsShot.writeData(Main.main.gameServer);
					}
					return true;

				}
			}

		}

		return false;
	}

	public void add(Entity e) {
		e.init(this);
		if (e instanceof Projectile) {
			projectiles.add((Projectile) e);
		} else if (e instanceof Weapon) {
			weapons.add((Weapon) e);
		} else {
			entites.add(e);
		}
	}

	// Grass = 0xff00ff00
	// Stone = 0xff515151
	// Void Tile = 0xff0016ff
	public Tile getTile(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height || tiles == null)
			return Tile.voidTile;
		if (tiles[x + y * width] == 0xFF00FF00)
			return Tile.grassTile;
		if (tiles[x + y * width] == 0xFF515151)
			return Tile.stoneTile;
		if (tiles[x + y * width] == 0xFFFF0000)
			return Tile.redBrickTile;
		if (tiles[x + y * width] == 0xFF000000)
			return Tile.greyBrickTile;
		if (tiles[x + y * width] == 0xFF0016FF)
			return Tile.waterTile;
		return Tile.voidTile;
	}

	public List<Projectile> getProjectiles() {
		return projectiles;
	}

	public List<Entity> getEntites() {
		return entites;
	}

	public List<Weapon> getWeapons() {
		return weapons;
	}

	public void removeNetPlayer(String userName) {
		int index = 0;
		for (Entity e : entites) {
			if (e instanceof NetPlayer && ((NetPlayer) e).getUserName().equals(userName)) {
				break;
			}
			if (e instanceof Player && ((Player) e).getUserName().equals(userName)) {
				break;
			}
			index++;
		}
		if (index >= entites.size()) {
			entites.remove(index - 1);
		} else {
			entites.remove(index);
		}

	}

	public int getNetPlayerIndex(String username) {
		int index = 0;
		for (Entity e : entites) {

			if (e instanceof NetPlayer && ((NetPlayer) e).getUserName().equals(username)) {
				break;
			}
			if (e instanceof Player && ((Player) e).getUserName().equals(username)) {
				break;
			}

			index++;
		}
		if (index >= entites.size()) {
			return index - 1;
		} else {
			return index;
		}
	}

	public void movePlayer(String username, int x, int y) {
		int index = getNetPlayerIndex(username);
		entites.get(index).x = x;
		entites.get(index).y = y;

	}
}
