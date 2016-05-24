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
import protocol_packet.OSPF.OSPF_LSU_Packet;
import protocol_packet.OSPF.OSPF_LSack_Packet;

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
				//主动发hello
				byte[] myrouter={0x01,0x01,0x03,0x01};
				byte[] myip={0x01,0x01,0x02,0x2};
				OSPF_Hello_Packet hello=packetTool.getInitHello(myrouter,myip, pack);
				//回应hello,建立邻居
				//OSPF_Hello_Packet hello=packetTool.getAnswerHello(pack);
				jpcap_util utilInstance=jpcap_util.getInstance(3);
				utilInstance.sendPacket(hello);
				System.out.println("send");
				//主动发起ex start
//				OSPF_DD_Packet ddpack=packetTool.getInitDD(pack);
//				utilInstance.sendPacket(ddpack);
				return;
			}
			if(pack instanceof OSPF_DD_Packet){//may be exstart need to return a suitable DD to establish master/slave relation
				OSPF_DD_Packet ddpack=null;
				if(((OSPF_DD_Packet)pack).DB_description>3){//init位为1,抢master
					ddpack=packetTool.getInitDD(pack);
				}
				else if(((OSPF_DD_Packet)pack).DB_description!=0){//init位不是1,继续完成exstart
					ddpack=packetTool.getAnwserDD(pack);
				}
				if(ddpack!=null){
					jpcap_util utilInstance=jpcap_util.getInstance(1);
					utilInstance.sendPacket(ddpack);
				}
				
			}
			if(pack instanceof OSPF_LSU_Packet){//need to answer lsack
				OSPF_LSack_Packet lsackpack=packetTool.getLSAck(pack);
				jpcap_util utilInstance=jpcap_util.getInstance(1);
				utilInstance.sendPacket(lsackpack);
			}
			
		}
	}
}
