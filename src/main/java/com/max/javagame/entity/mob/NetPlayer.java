package com.max.javagame.entity.mob;

import java.awt.event.KeyEvent;
import java.net.InetAddress;

import com.max.javagame.entity.AssaultRifle;
import com.max.javagame.entity.Gun;
import com.max.javagame.entity.MachineGun;
import com.max.javagame.entity.Weapon;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Render;
import com.max.javagame.input.Keyboard;
import com.max.javagame.input.Mouse;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.net.packets.Packet01Disconnect;
import com.max.javagame.net.packets.Packet02Move;

public class NetPlayer extends Player{
	
	public InetAddress ipAddress;
	public int port;
	public Keyboard keyboard;
	
	public int healthPoints = 100;
	
	public NetPlayer(String username, Sprite sprite, int x, int y, Keyboard input, InetAddress ipAddress, int port) {
		super(username,sprite, x, y, input);
		
		this.ipAddress = ipAddress;
		this.port = port;
	}
	public NetPlayer(String username, Sprite sprite, int x, int y, InetAddress ipAddress, int port) {
		super(username,sprite, x, y);
	
		this.ipAddress = ipAddress;
		this.port = port;
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
			Weapon fireBallCannon = new AssaultRifle(getUserName());
			if (inventory[selectedItemId] == null) {
				inventory[selectedItemId] = fireBallCannon;
			}

			level.add(fireBallCannon);
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
			ya -= 1;
		}
		if (input != null && input.keys[KeyEvent.VK_S]) {
			ya += 1;
		}
		if (input != null && input.keys[KeyEvent.VK_A]) {
			xa -= 1;
		}
		if (input != null && input.keys[KeyEvent.VK_D]) {
			xa += 1;
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
		if (input != null && input.keys[KeyEvent.VK_4]) {
			
		}

		if (xa != 0 || ya != 0) {
			move(xa, ya);
			walking = true;
			if(Main.main.gameClient != null) {
				Packet02Move packet = new Packet02Move(getUserName(),(int)x,(int)y);
				packet.writeData(Main.main.gameClient);
			}
		
		} else {
			walking = false;
		}
		clearProjectiles();

		if (Mouse.getB() == 1 && inventory[selectedItemId] != null) {
			inventory[selectedItemId].shoot((int) x, (int) y);
		}
	}
	protected void checkPlayerHealthPoints() {
		if (healthPoints <= 0) {
			healthPoints = 0;
			if(input != null) {
				Packet01Disconnect playerElim = new Packet01Disconnect(getUserName());
				playerElim.writeData(Main.main.gameClient);
				remove();
			}

		}
	}
	public void updateHealth() {
		if (healthPoints < 100) {
			healthTimer--;
			if (healthTimer <= 0) {
				healthPoints++;
				healthTimer = 60;
			}

		}
		
	}
	public void render(Render renderer) {
		renderer.renderSprite((int)x, (int)y, Sprite.playerSprite);
		renderInventory(renderer);
	}
}
