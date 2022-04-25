package com.max.javagame.graphics;

public class Sprite {
	public final int widthSize, heightSize;
	private int x, y;
	public int pixels[];
	private SpriteSheet sheet;
	
	public static Sprite grassSprite = new Sprite(16,16, 0, 0, SpriteSheet.tilesSprite); 
	public static Sprite stoneSprite = new Sprite(16,16, 1, 0, SpriteSheet.tilesSprite); 
	public static Sprite redBrickSprite = new Sprite(16,16, 2, 0, SpriteSheet.tilesSprite); 
	public static Sprite greyBrickSprite = new Sprite(16,16, 3, 0, SpriteSheet.tilesSprite); 
	public static Sprite waterSprite = new Sprite(16,16, 4, 0, SpriteSheet.tilesSprite); 
	public static Sprite voidSprite = new Sprite(16,16,0);
	
	public static Sprite playerSprite = new Sprite(16,16,0,0,SpriteSheet.playerSprite);
	public static Sprite teamPlayerSprite = new Sprite(16,16,1,0,SpriteSheet.playerSprite);
	public static Sprite enemyPlayerSprite = new Sprite(16,16,2,0,SpriteSheet.playerSprite);
	
	public static Sprite inventoryBoxEmpty = new Sprite(16,16,0,0,SpriteSheet.inventory);
	public static Sprite inventorySelected = new Sprite(16,16,0,1,SpriteSheet.inventory);
	public static Sprite inventoryBoxFireball = new Sprite(16,16,1,0,SpriteSheet.inventory);
	public static Sprite inventoryBoxMachineGun = new Sprite(16,16,2,0,SpriteSheet.inventory);
	public static Sprite inventoryBoxAssaultRifle = new Sprite(16,16,3,0,SpriteSheet.inventory);
	
	public static Sprite fireballSprite = new Sprite(16,16,0,0,SpriteSheet.projectileBox);
	public static Sprite bulletSprite = new Sprite(16,16,2,0,SpriteSheet.projectileBox);
	
	public static Sprite machineGunDrop = new Sprite(16,16,0,0,SpriteSheet.weaponDrops);
	public static Sprite assaultRifleDrop = new Sprite(16,16,1,0,SpriteSheet.weaponDrops);
	public static Sprite fireballDrop = new Sprite(16,16,2,0,SpriteSheet.weaponDrops);
	
	
	//Width, Height, X, Y
	public Sprite(int width, int height, int x, int y, SpriteSheet sheet) {
		widthSize = width;
		heightSize = height;
		pixels = new int[width*height];
		this.x = x*16; //Coordinates for tiles
		this.y = y*16;
		this.sheet = sheet;
		load();
	}
	public Sprite(int width, int height, int color) {
		widthSize = width;
		heightSize = height;
		pixels = new int[width*width];
		setColor(color);
	}

	private void setColor(int color) {
		for(int i = 0; i< pixels.length;i++) {
			pixels[i] = color;
		}
	}
	private void load() {
		for(int y = 0; y < heightSize; y++) {
			for(int x = 0; x<widthSize; x++) {
				pixels[x+y*widthSize] = sheet.pixels[(x+this.x)+(y+this.y)*sheet.size];
				
			}
		}
	}
}
