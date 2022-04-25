package com.max.javagame.net.packets;


import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;

public class Packet01Disconnect extends Packet{
	private String username;
	
	public Packet01Disconnect(byte[] data) {
		super(01);
		this.username = readData(data);
	}
	public Packet01Disconnect(String username) {
		super(01);
		this.username = username;
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
		return ("01"+this.username).getBytes();
	}
	
	public String getUserName() {
		return username;
	}
}
