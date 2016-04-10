package jpcap_util;

import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;

public class callback_reciever implements PacketReceiver {
	@Override
	public void receivePacket(Packet p) {
		// TODO Auto-generated method stub
		//IP packet only
		if(p instanceof IPPacket){
//			IPPacket ippack=(IPPacket)p;
			
			System.out.println(p.toString());
			System.out.print("\theader : ");
			for(byte i: p.header){
				System.out.printf("%x ",i);
			}
			System.out.print("\n\t\tdata : ");
			for(byte i:p.data){
				System.out.printf("%x ",i);
			}
			System.out.println();
		}
	}
}
