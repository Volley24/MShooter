package com.max.javagame.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import com.max.javagame.entity.mob.NetPlayer;
import com.max.javagame.entity.mob.Player;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.net.packets.Packet;
import com.max.javagame.net.packets.Packet00Login;
import com.max.javagame.net.packets.Packet01Disconnect;
import com.max.javagame.net.packets.Packet02Move;
import com.max.javagame.net.packets.Packet03DamagePlayer;
import com.max.javagame.net.packets.Packet.PacketTypes;

public class GameClient extends Thread {

	private InetAddress address;// IP address to the server we are connecting to
	private DatagramSocket socket; // The actual socket that will contain information
	private int port;
	private Main main;

	public GameClient(Main main, String ipAddress, int port) {
		this.setName("Client-Thread");
		this.port = port;
		this.main = main;
		try {
			socket = new DatagramSocket();
			address = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException | SocketException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			// Listening for com.max.javagame.input:
			byte[] data = new byte[1024]; // Actual array of bytes of data Limit of 1024 bytes.
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);// Recieve data
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(),packet.getAddress(),packet.getPort());

//			String message = new String(packet.getData());
//			System.out.println("Server: " + message);
		}

	}
	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(msg.substring(0,2));
		Packet packet;
		switch (type) {
			default:
			case INVALID:

				break;
			case LOGIN:
				packet = new Packet00Login(data);
				
				System.out.println(((Packet00Login) packet).getUserName()+"["+address.getHostAddress()+":"+port+"]"+" has joined the game");
				
				NetPlayer player = new NetPlayer(((Packet00Login) packet).getUserName(),Sprite.enemyPlayerSprite, ((Packet00Login) packet).getX(), ((Packet00Login) packet).getY(), address, port);
				
				main.level.add(player);

				break;
			case DISCONNECT:
				packet = new Packet01Disconnect(data);

				System.out.println("["+address.getHostAddress()+":"+port+"]"+"("+((Packet01Disconnect) packet).getUserName()+")"+" has disconnected.");
				
				main.level.removeNetPlayer(((Packet01Disconnect)packet).getUserName());
				break;
				
			case MOVE:
				packet = new Packet02Move(data);
				handlePacket((Packet02Move) packet);
				break;
			case DAMAGE_PLAYER:
				Packet03DamagePlayer damagePacket = new Packet03DamagePlayer(data);
				int currentHealth = ((Player)main.level.getEntites().get(main.level.getNetPlayerIndex(damagePacket.getUserName()))).getHealthPoints();
				int damage = damagePacket.getDamage();
				((Player) main.level.getEntites().get(main.level.getNetPlayerIndex(damagePacket.getUserName()))).setHealth(currentHealth-damage);
				break;
		}
	}
	private void handlePacket(Packet02Move movePacket) {
		main.level.movePlayer(movePacket.getUserName(), movePacket.getX(), movePacket.getY());
		
	}

	public void sendData(byte[] data) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
