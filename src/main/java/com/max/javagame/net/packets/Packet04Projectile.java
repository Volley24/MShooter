package com.max.javagame.net.packets;

import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;

public class Packet04Projectile extends Packet{
	//This packet's purpose is to send projectile data to the other client
	private String username;
	private int startX;
	private int startY;
	private int speed;
	private int angle;
	
	public Packet04Projectile(byte[] data) {
		super(04);
		String[] dataArray = readData(data).split(",");
		username = dataArray[0];
		startX = Integer.parseInt(dataArray[1]);
		startY = Integer.parseInt(dataArray[2]);
		speed = Integer.parseInt(dataArray[3]);
		angle = Integer.parseInt(dataArray[4]);

	}
	public Packet04Projectile(String username, int startX, int startY, int speed, int angle) {
		super(04);
		this.username = username;
		this.startX = startX;
		this.startY = startY;
		this.speed = speed;

	}
	@Override
	public void writeData(GameClient client) {

		
	}

	@Override
	public void writeData(GameServer server) {
	
		
	}

	@Override
	public byte[] getData() {
		
		return ("04"+username+","+startX+","+startY+","+speed+","+angle).getBytes();
	}

}
