package com.max.javagame.net.packets;

import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;

public class Packet00Login extends Packet{
	private String username;
	private int x;
	private int y;
	
	public Packet00Login(byte[] data) {
		super(00);
		String[] dataArray = readData(data).split(",");
        this.username = dataArray[0];
        this.x = Integer.parseInt(dataArray[1]);
        this.y = Integer.parseInt(dataArray[2]);
	}
    public Packet00Login(String username, int x, int y) {
        super(00);
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
		return ("00" + this.username + "," + getX() + "," + getY()).getBytes();
	}
	
	public int getY() {
		return x;
	}
	public int getX() {
		return y;
	}
	public String getUserName() {
		return username;
	}

}
