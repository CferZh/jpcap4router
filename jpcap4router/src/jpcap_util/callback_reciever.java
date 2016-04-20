package jpcap_util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import protocol_packet.OSPF_packet;
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
			
			if(pack instanceof OSPF_Hello_Packet){//ospf hello -> need to return a suitable hello to build neighor
				OSPF_Hello_Packet hello=new OSPF_Hello_Packet();
				byte dst[]={(byte) 0xE0,(byte)0x00,(byte)0x00,(byte)0x05};//broadcast dst 224.0.0.5
				/**
				 * use the information in captured packet to reconstruct new hello packet to send
				 */
				try {
					hello.setIPv4Parameter(pack.priority,pack.d_flag,pack.t_flag,pack.r_flag,pack.rsv_tos,pack.rsv_frag
							,pack.dont_frag,pack.more_frag,pack.offset,pack.ident,100,packet_factory.OSPF_PAKET,
							InetAddress.getLocalHost(), InetAddress.getByAddress(dst));
					byte[] sr={0x01,0x01,0x01,0x01};
					byte[] aid={0x01,0x01,0x01,0x01};
					byte[] authData={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
					hello.setAllOSPFHeader(2, 1, 0, sr, aid, 0, authData);
					byte[] mask={(byte) 0xff,(byte)0xff,(byte)0xff,0x00};
					hello.setNetMask(mask);
					hello.setHelloInterval(10);
					hello.setOptions((byte)0x04);
					hello.setRouterPriority((byte)0x01);
					hello.setRouterDeadInterval(40);
					byte[] designID={0x00,0x00,0x00,0x00};
					byte[] bkID={0x00,0x00,0x00,0x00};
					hello.setDesignedRouter(designID);
					hello.setBKDesignedRouter(bkID);
					byte[] neighbors=((OSPF_Hello_Packet) pack).source_router;
					hello.setActiveNeighbor(neighbors);
					int len=hello.getSendableData().length;
					hello.setPackLen(len);
					hello.setChecksum(util.util.getchecksum(hello.getSendableData()));
					System.out.println("hello pack to send:");
					for(int i=0;i<len;i++){						
						System.out.printf("%x ",hello.getSendableData()[i]);
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	}
}
