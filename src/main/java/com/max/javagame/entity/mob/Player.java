package com.max.javagame.entity.mob;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;

import com.max.javagame.entity.AssaultRifle;
import com.max.javagame.entity.Fireball;
import com.max.javagame.entity.Gun;
import com.max.javagame.entity.MachineGun;
import com.max.javagame.entity.SniperRifle;
import com.max.javagame.entity.Weapon;
import com.max.javagame.entity.weapon.projectile.Projectile;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.input.Keyboard;
import com.max.javagame.input.Mouse;
import com.max.javagame.net.packets.Packet01Disconnect;
import com.max.javagame.net.packets.Packet02Move;

public class Player extends Mob {
	protected String username;

	protected Weapon[] inventory;
	protected int selectedItemId = 0;

	protected Keyboard input;
	public Sprite sprite;

	private int playerSpeedModifier = 1;
	protected int healthTimer = 60;

	public Player(String username,Sprite sprite, int x, int y, Keyboard input) {
		super(x, y, sprite, 100);

		this.username = username;
		this.input = input;
		inventory = new Weapon[3];
	}                                                              

	public Player(String username,Sprite sprite, int x, int y) {
		super(x, y, sprite, 100);
		this.username = username;
		inventory = new Weapon[3];
	}

	public void update() {

		if (inventory[selectedItemId] != null && input.keys[KeyEvent.VK_Q]) {
			// Weapon will drop if you are holding something.
			inventory[selectedItemId].dropWeapon((int) x, (int) y);
			if (inventory[selectedItemId] instanceof Gun) {
				((Gun) inventory[selectedItemId]).cancelReload();
			}

			inventory[selectedItemId] = null;
		}
		if (input != null && inventory[selectedItemId] == null && input.keys[KeyEvent.VK_F]) {
			// You will pickup a weapon if you are not holding anything in your selected
			// slot AND are within reach of the weapon
			for (Weapon weapon : level.getWeapons()) {
				if (weapon.canPlayerReachWeapon(this)) {
					weapon.pickupWeapon();
					inventory[selectedItemId] = weapon;
					break;
				}
			}

		}

		if (input != null && input.keys[KeyEvent.VK_K]) {
			Gun sniperRifle = new SniperRifle(getUserName());
			if (inventory[selectedItemId] == null) {
				inventory[selectedItemId] = sniperRifle;
			}

			level.add(sniperRifle);
		}

		if (input != null && input.keys[KeyEvent.VK_J]) {
			Weapon machineGun = new MachineGun(getUserName());
			if (inventory[selectedItemId] == null) {
				inventory[selectedItemId] = machineGun;
			}

			level.add(machineGun);
		}

		checkPlayerHealthPoints();
		updateHealth();

		int xa = 0;
		int ya = 0;
		if (input != null && input.keys[KeyEvent.VK_W]) {
			ya -= 1 * playerSpeedModifier;
		}
		if (input != null && input.keys[KeyEvent.VK_S]) {
			ya += 1 * playerSpeedModifier;
		}
		if (input != null && input.keys[KeyEvent.VK_A]) {
			xa -= 1 * playerSpeedModifier;
		}
		if (input != null && input.keys[KeyEvent.VK_D]) {
			xa += 1 * playerSpeedModifier;
		}
		if (input != null && input.keys[KeyEvent.VK_R] && inventory[selectedItemId] != null) {
			if (inventory[selectedItemId] instanceof Gun) {
				((Gun) inventory[selectedItemId]).reload();
			}
		}

		for (Weapon weapon : inventory) {
			if (inventory[selectedItemId] == weapon) {
				continue;
			}
			if (weapon != null && weapon instanceof Gun && ((Gun) weapon).isReloading) {
				((Gun) weapon).cancelReload();
			}
		}

		if (input != null && input.keys[KeyEvent.VK_1]) {
			selectedItemId = 0;
		}
		if (input != null && input.keys[KeyEvent.VK_2]) {
			selectedItemId = 1;
		}
		if (input != null && input.keys[KeyEvent.VK_3]) {
			selectedItemId = 2;
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
			if(Main.main.gameServer != null) {
				Packet02Move packet = new Packet02Move(getUserName(),(int)x,(int)y);
				packet.writeData(Main.main.gameServer);
			}
		
		} else {
			walking = false;
		}
		clearProjectiles();

		if (Mouse.getB() == 1 && inventory[selectedItemId] != null) {
			inventory[selectedItemId].shoot((int) x, (int) y);
		}

	}

	public void render(Render renderer) {
		sprite = Sprite.playerSprite;

		renderer.renderSprite((int) x, (int) y, sprite);
		renderInventory(renderer);

	}

	public void clearProjectiles() {
		for (int i = 0; i < level.getProjectiles().size(); i++) {
			Projectile p = level.getProjectiles().get(i);
			if (p.isRemoved()) {
				level.getProjectiles().remove(i);
			}
		}

	}

	public void updateHealth() {
		if (healthPoints < 100) {
			healthTimer--;
			if (healthTimer <= 0) {
				//healthPoints++;
				healthTimer = 60;
			}
		}
	}

	public void renderPlayerName(Graphics g) {
		g.drawString(getUserName(), (int) Main.getWindowWidth() / 2 - 45, (int) (Main.getWindowHeight() / 2 - 48 - 16));
	}

	public void renderPlayerInfo(Graphics g) {
		Color oldColor = g.getColor();
		Font oldFont = g.getFont();

		g.setColor(Color.RED);
		g.setFont(new Font("Verdana", Font.BOLD, 20));
		g.drawString("HP: " + String.valueOf(getHealthPoints()), (int) Main.getWindowWidth() / 2 - 45,
				(int) (Main.getWindowHeight() / 2 - 48 - 16));

		g.setColor(oldColor);
		g.setFont(oldFont);
	}

	public void drawWeaponInfo(Graphics g) {
		DecimalFormat format = new DecimalFormat("#.#");
		g.setColor(Color.WHITE);
		Font f = new Font("Verdana", Font.BOLD, 25);
		g.setFont(f);

		if (inventory[selectedItemId] != null && inventory[selectedItemId] instanceof Gun) {
			if(((Gun) inventory[selectedItemId]).isReloading){
				g.drawString(
						inventory[selectedItemId].getName() + " | Ammo: " + ((Gun) inventory[selectedItemId]).ammoLoaded+" Reloading: "+ format.format(((Gun) inventory[selectedItemId]).reloadTimer/60),
						250, 420);
			}else {
			g.drawString(
					inventory[selectedItemId].getName() + " | Ammo: " + ((Gun) inventory[selectedItemId]).ammoLoaded,
					250, 420);
			}
		}
		if (inventory[selectedItemId] != null && inventory[selectedItemId] instanceof Weapon
				&& !(inventory[selectedItemId] instanceof Gun)) {
			g.drawString(inventory[selectedItemId].getName() + " | Cooldown: "
					+ format.format(inventory[selectedItemId].getWeaponCooldown() / 60), 250, 420);
		}
		g.setColor(Color.WHITE);
		g.setFont(new Font("Verdana", 20, 20));
	}

	protected void checkPlayerHealthPoints() {
		if (healthPoints <= 0) {
			healthPoints = 0;
			if(Main.main.gameServer != null) {
				Packet01Disconnect playerElim = new Packet01Disconnect(getUserName());
				playerElim.writeData(Main.main.gameServer);
				remove();
			}

		}
	}

	protected void renderInventory(Render renderer) {
		for (int i = 0; i < inventory.length; i++) {
			if (inventory[i] != null) {
				if (inventory[i] instanceof MachineGun) {
					renderer.renderSprite((Main.windowWidth / 2) + (15 * i) - 15 - 15 + renderer.xOffset,
							Main.windowHeight - 16 + renderer.yOffset, Sprite.inventoryBoxMachineGun);
				} else if (inventory[i] instanceof Fireball) {
					renderer.renderSprite((Main.windowWidth / 2) + (15 * i) - 15 - 15 + renderer.xOffset,
							Main.windowHeight - 16 + renderer.yOffset, Sprite.inventoryBoxFireball);
				} else if (inventory[i] instanceof AssaultRifle) {
					renderer.renderSprite((Main.windowWidth / 2) + (15 * i) - 15 - 15 + renderer.xOffset,
							Main.windowHeight - 16 + renderer.yOffset, Sprite.inventoryBoxAssaultRifle);
				}
			} else {
				renderer.renderSprite((Main.windowWidth / 2) + (15 * i) - 15 - 15 + renderer.xOffset,
						Main.windowHeight - 16 + renderer.yOffset, Sprite.inventoryBoxEmpty);
			}
			if (i == selectedItemId) {
				renderer.renderSprite((Main.windowWidth / 2) + (15 * i) - 15 - 15 + renderer.xOffset,
						Main.windowHeight - 16 + renderer.yOffset, Sprite.inventorySelected);
			}

		}
	}

	// Getters and Setters:
	public int getHealthPoints() {
		return healthPoints;
	}

	public String getUserName() {
		return username;
	}

	public Weapon[] getInventory() {
		return inventory;
	}

	public Weapon getSelectedWeapon() {
		return inventory[selectedItemId];
	}

	@Override
	public void setHealth(int newHealth) {
		healthPoints = newHealth;
	}

}
