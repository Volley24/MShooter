package com.max.javagame.net.packets;


import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;

public class Packet02Move extends Packet{
	private String username;
	private int x, y;
	
	public Packet02Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
	}
	public Packet02Move(String username, int x, int y) {
		super(02);
		this.username = username;
		this.x = x;
		this.y = y;
	}

	@Override
	public void writeData(GameClient client) {
		client.sendData(getData());//Send data to server
	}

	@Override
	public void writeData(GameServer server) {
		server.sendDataToAllClients(getData());//Send data to all clients
	}
	@Override
	public byte[] getData() {
		//Example: 02Max,2,3
		return ("02"+this.username+","+this.x+","+this.y).getBytes();
	}
	
	public String getUserName() {
		return username;
	}
	
	public int getX() {
		return this.x;
	}
	public int getY() {
		return this.y;
	}
}
