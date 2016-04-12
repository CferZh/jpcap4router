package jpcap_util;

import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import protocol_packet.packet_factory;
import protocol_packet.OSPF.OSPF_Hello_Packet;

public class callback_reciever implements PacketReceiver {
	@Override
	public void receivePacket(Packet p) {
		// TODO Auto-generated method stub
		//IP packet only
		if(p instanceof IPPacket){
//			IPPacket ippack=(IPPacket)p;
			/**
			 * test of protocol packet content in class IPPacket
			 */
//			if (((IPPacket) p).protocol==0x59) {
//				System.out.println(p.toString());
//				System.out.print("\theader : ");
//				for (byte i : p.header) {
//					System.out.printf("%x ", i);
//				}
//				System.out.print("\n\t\tdata : ");
//				for (byte i : p.data) {
//					System.out.printf("%x ", i);
//				}
//				System.out.println();
//			}
			
			
			IPPacket pack=packet_factory.createPacket((IPPacket) p);
			
			if(pack instanceof OSPF_Hello_Packet){
				System.out.println("there is a ospf hello");
				for(int i=0;i<4;i++)
					System.out.printf("%x ",((OSPF_Hello_Packet)pack).designed_router[i]);
				System.out.println();
			}
			
		}
	}
}
