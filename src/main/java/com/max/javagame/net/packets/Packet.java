package com.max.javagame.net.packets;


import com.max.javagame.net.GameClient;
import com.max.javagame.net.GameServer;

public abstract class Packet {
	public static enum PacketTypes{
		
		INVALID(-1), LOGIN(00), DISCONNECT(01), MOVE(02), DAMAGE_PLAYER(03),SEND_PROJECTILE(04);
		
		
		private int packetId;
		private PacketTypes(int packetId) {
			this.packetId = packetId;
		}
		
		public int getPacketId() {
			return packetId;
		}
	}
	public byte packetId;
	
	public Packet(int packetId) {
		this.packetId = (byte)packetId;
	}
	
	public abstract void writeData(GameClient client);
	public abstract void writeData(GameServer server);

	
	public String readData(byte[] data) {
		String msg = new String(data).trim();
		return msg.substring(2);
	}
	public abstract byte[] getData();
	
	public static PacketTypes lookupPacket(int packetId) {
		for(PacketTypes p: PacketTypes.values()) {
			if(p.getPacketId() == packetId) {
				return p;
			}
		}
		return PacketTypes.INVALID;
	}
	
	public static PacketTypes lookupPacket(String packetId) {
		try {
			return lookupPacket(Integer.parseInt(packetId));
		}catch(NumberFormatException e) {
			return PacketTypes.INVALID;
		}
	}
}
