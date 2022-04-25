package com.max.javagame.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import com.max.javagame.entity.mob.NetPlayer;
import com.max.javagame.entity.mob.Player;
import com.max.javagame.graphics.Main;
import com.max.javagame.graphics.Sprite;
import com.max.javagame.net.packets.Packet;
import com.max.javagame.net.packets.Packet.PacketTypes;
import com.max.javagame.net.packets.Packet00Login;
import com.max.javagame.net.packets.Packet01Disconnect;
import com.max.javagame.net.packets.Packet02Move;
import com.max.javagame.net.packets.Packet03DamagePlayer;

public class GameServer extends Thread {
	private DatagramSocket socket; // UDP Socket
	private List<NetPlayer> connectedPlayers = new ArrayList<NetPlayer>();
	private Main main;

	public GameServer(Main main, int port) {
		this.setName("Server-Thread");
		this.main = main;
		try {
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);// Packet that is sent to the server, we put
																			// the data (byte array) inside
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());

		}

	}

	private void parsePacket(byte[] data, InetAddress address, int port) {
		String msg = new String(data).trim();
		PacketTypes type = Packet.lookupPacket(msg.substring(0, 2));
		Packet packet;
		switch (type) {
		default:
		case INVALID:
			break;
		case LOGIN:
			Packet00Login packetNew = new Packet00Login(data);
			int x = packetNew.getX();
			int y = packetNew.getY();

			String username = packetNew.getUserName();

			System.out.println("[" + address.getHostAddress() + ":" + port + "]" + "(" + username + ")"
					+ " has connected at position x: " + x + " | y: " + y);

			NetPlayer player = new NetPlayer(username, Sprite.enemyPlayerSprite, x, y, address, port);

			connectedPlayers.add(player);
			main.level.add(player);
			sendData(new Packet00Login(Main.player.getUserName(), (int) Main.player.x, (int) Main.player.y).getData(),
					player.ipAddress, player.port);
			for (NetPlayer p : connectedPlayers) {

				if (!(p.getUserName().equalsIgnoreCase(player.getUserName()))) {
					sendData(new Packet00Login(p.getUserName(), (int) p.x, (int) p.y).getData(), player.ipAddress,
							player.port);
					sendData(new Packet00Login(player.getUserName(), (int) player.x, (int) player.y).getData(),
							p.ipAddress, p.port);

				}

			}
			break;
		case DISCONNECT:
			packet = new Packet01Disconnect(data);
			System.out.println("[" + address.getHostAddress() + ":" + port + "]" + "("
					+ ((Packet01Disconnect) packet).getUserName() + ")" + " has disconnected.");

			// NetPlayer playerToRemove = getNetPlayer(packet.getUserName());
			this.connectedPlayers.remove(getNetPlayerIndex(((Packet01Disconnect) packet).getUserName()));
			main.level.removeNetPlayer(((Packet01Disconnect) packet).getUserName());
			packet.writeData(this);
			break;
		case MOVE:
			packet = new Packet02Move(data);
			// System.out.println(movePacket.getUserName()+" has moved to x:"
			// +movePacket.getX()+" | y: "+movePacket.getY());
			handleMove((Packet02Move) packet);
			break;
		case DAMAGE_PLAYER:
			Packet03DamagePlayer damagePacket = new Packet03DamagePlayer(data);
			int currentHealth = ((Player) main.level.getEntites().get(getNetPlayerIndex(damagePacket.getUserName())))
					.getHealthPoints();
			int damage = damagePacket.getDamage();

			((Player) main.level.getEntites().get(getNetPlayerIndex(damagePacket.getUserName())))
					.setHealth(currentHealth - damage);
			break;
		}
	}

	private void handleMove(Packet02Move movePacket) {

		if (getNetPlayer(movePacket.getUserName()) != null) {
			int index = getNetPlayerIndex(movePacket.getUserName());
			this.connectedPlayers.get(index).x = movePacket.getX();
			this.connectedPlayers.get(index).y = movePacket.getY();
			movePacket.writeData(this);
		}
	}

	public NetPlayer getNetPlayer(String username) {
		for (NetPlayer p : connectedPlayers) {
			if (p.getUserName().equals(username)) {
				return p;
			}
		}
		return null;
	}

	public int getNetPlayerIndex(String username) {
		int i = 0;
		for (NetPlayer p : connectedPlayers) {
			if (p.getUserName().equals(username)) {
				break;
			}
			i++;
		}
		if (i >= this.connectedPlayers.size()) {
			return i - 1;
		} else {
			return i;
		}
	}

	public void sendData(byte[] data, InetAddress address, int port) {
		DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (NetPlayer p : connectedPlayers) {
			sendData(data, p.ipAddress, p.port);
		}

	}
}
