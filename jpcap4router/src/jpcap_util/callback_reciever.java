package jpcap_util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.JpcapSender;
import jpcap.PacketReceiver;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import packetTool.packetTool;
import protocol_packet.OSPF_packet;
import protocol_packet.packet_factory;
import protocol_packet.OSPF.OSPF_DD_Packet;
import protocol_packet.OSPF.OSPF_Hello_Packet;

public class callback_reciever implements PacketReceiver {
	@Override
	public void receivePacket(Packet p) {
		// TODO Auto-generated method stub
		//IP packet only
		if(p instanceof IPPacket){
			if(packetTool.isSendByMe(p)){//filter packet sent by localhost
				return;
			}
			IPPacket pack=packet_factory.createPacket((IPPacket) p);
			if(pack instanceof OSPF_Hello_Packet){//ospf hello -> need to return a suitable hello to build neighor
				
				OSPF_Hello_Packet hello=packetTool.getAnswerHello(pack);
				jpcap_util utilInstance=jpcap_util.getInstance(3);
				utilInstance.sendPacket(hello);
				//主动发起ex start
//				OSPF_DD_Packet ddpack=packetTool.getInitDD(pack);
//				utilInstance.sendPacket(ddpack);
				return;
			}
			if(pack instanceof OSPF_DD_Packet){//may be exstart need to return a suitable DD to establish master/slave relation
				OSPF_DD_Packet ddpack=packetTool.getInitDD(pack);
				jpcap_util utilInstance=jpcap_util.getInstance(1);
				utilInstance.sendPacket(ddpack);
			}
			
		}
	}
}
