package com.max.javagame.net.packets;

import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;

public class Packet03DamagePlayer extends Packet{
	private String username;
	private int damage;
	
	public Packet03DamagePlayer(byte[] data) {
		super(03);
		String[] dataArray = readData(data).split(",");
		//03[0],Max[1],100[2]
		
		this.username = dataArray[0];
		this.damage = Integer.parseInt(dataArray[1]);
	}
	public Packet03DamagePlayer(String username, int damage) {
		super(03);
		this.username = username;
		this.damage = damage;
	}

	@Override
	public void writeData(GameClient client) {
		//String msg = new String(getData());
		//System.out.println(msg);
		client.sendData(getData());
		
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());
		
	}

	@Override
	public byte[] getData() {
		// Example: 03,Max,100
		return ("03"+username+","+damage).getBytes();
	}
	public String getUserName() {
		return username;
	}
	public int getDamage() {
		return damage;
	}
}
