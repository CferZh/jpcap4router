package packetTool;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap_util.jpcap_util;
import protocol_packet.packet_factory;
import protocol_packet.OSPF.OSPF_Hello_Packet;

public class packetTool {
	/**
	 * based on recived hello packet building answer hello packet 
	 * @param pack
	 * @return
	 */
	public static OSPF_Hello_Packet getAnswerHello(IPPacket pack){
		OSPF_Hello_Packet hello=new OSPF_Hello_Packet();
		byte dstIP[]={(byte) 0xE0,(byte)0x00,(byte)0x00,(byte)0x05};//broadcast dst 224.0.0.5
		byte dstMAC[]={0x01,0x00,0x5e,0x00,0x00,0x05};//ipv4mcast
		/**
		 * use the information in captured packet to reconstruct new hello packet to send
		 */
		try {
			/**
			 * ip header
			 */
			hello.setIPv4Parameter(pack.priority,pack.d_flag,pack.t_flag,pack.r_flag,pack.rsv_tos,pack.rsv_frag
					,pack.dont_frag,pack.more_frag,pack.offset,pack.ident,1,packet_factory.OSPF_PAKET,
					InetAddress.getLocalHost(), InetAddress.getByAddress(dstIP));
			/**
			 * ospf header
			 */
			byte[] sr={0x01,0x01,0x01,0x01};
			byte[] aid={0x00,0x00,0x00,0x00};
			byte[] authData={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			hello.setAllOSPFHeader(2, 1, 0, sr, aid, 0, authData);
			/**
			 * ospf hello content
			 */
			hello.setNetMask(((OSPF_Hello_Packet) pack).net_mask);
			hello.setHelloInterval(10);
			hello.setOptions((byte)0x12);
			hello.setRouterPriority((byte)0x01);
			hello.setRouterDeadInterval(40);
			byte[] designID={0x0a,0x6d,0x17,(byte) 0x9f};
			byte[] bkID={0x00,0x00,0x00,0x00};
			hello.setDesignedRouter(designID);
			hello.setBKDesignedRouter(bkID);
			byte[] neighbors=((OSPF_Hello_Packet) pack).source_router;
			hello.setActiveNeighbor(neighbors);//set active neighbor
			//reset len & checksum in ospf header 
			int len=hello.getSendableData().length-12;
			hello.setPackLen(len);
			hello.setChecksum(util.util.getchecksum(hello.getSendableData()));
			//set data field in ip packet
			hello.data=hello.getSendableData();
			/**
			 * set datalink header
			 */
			EthernetPacket ether=new EthernetPacket();
			ether.frametype=EthernetPacket.ETHERTYPE_IP;
			ether.src_mac=jpcap_util.getMyMac();
			ether.dst_mac=dstMAC;
			//ether.dst_mac=dst;
			hello.datalink=ether;
			jpcap_util utilInstance=jpcap_util.getInstance(1);
			
			utilInstance.sendPacket(hello);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hello;
	}
	public static boolean isSendByMe(Packet p){
		try {
			if(((IPPacket) p).src_ip.equals(InetAddress.getLocalHost())){//filter packets send from localhost
				return true;
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
}
