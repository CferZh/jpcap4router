package packetTool;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.DatalinkPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap_util.jpcap_util;
import protocol_packet.OSPF_packet;
import protocol_packet.packet_factory;
import protocol_packet.OSPF.OSPF_DD_Packet;
import protocol_packet.OSPF.OSPF_Hello_Packet;
import protocol_packet.OSPF.OSPF_LSU_Packet;
import protocol_packet.OSPF.OSPF_LSack_Packet;

public class packetTool {
	/**
	 * based on recived hello packet building answer hello packet 
	 * @param pack
	 * @return
	 */
	@Deprecated
	public static OSPF_Hello_Packet getInitHello(byte[] MyRouterID,byte[] MyIP,IPPacket pack){
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
			//使用参数1作为routerid
			byte[] sr=new byte[4];
			for(int i=0;i<4;i++){
				sr[i]=MyRouterID[i];
			}
			
		
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
//			byte[] neighbors=((OSPF_Hello_Packet) pack).source_router;
//			hello.setActiveNeighbor(neighbors);//set active neighbor
			//reset len & checksum in ospf header 
		//	int len=hello.getSendableData().length-12;
		//	hello.setPackLen(len);
//			hello.setChecksum(util.util.getchecksum(hello.getSendableData()));
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
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hello;
	}
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
			//使用默认source routerid
			byte[] sr={(byte) 0xfe,(byte) 0xfe,(byte) 0xfe,(byte) 0xfe};
			//使用比对方大的source routerid
//			byte[] sr=new byte[4];
//			for(int i=0;i<4;i++){
//				sr[i]=((OSPF_packet)pack).source_router[i];
//			}
//			sr[3]+=1;
		
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
		//	int len=hello.getSendableData().length-12;
		//	hello.setPackLen(len);
//			hello.setChecksum(util.util.getchecksum(hello.getSendableData()));
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
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return hello;
	}
	/**
	 * 用于自身routerid 大于对方抢夺master时
	 * @param pack
	 * @return
	 */
	@Deprecated
	public static OSPF_DD_Packet getInitDD(IPPacket pack){
		OSPF_DD_Packet ddpack=new OSPF_DD_Packet();
//		byte dstIP[]={(byte) 0xE0,(byte)0x00,(byte)0x00,(byte)0x05};//broadcast dst 224.0.0.5
//		byte dstMAC[]={(byte)0xc4,0x04,0x08,0x48,0x00,0x10};//ipv4mcast
		byte dstMac[]=new byte[6];
		for(int i=0;i<6;i++){
			dstMac[i]=pack.header[6+i];
		}
		/**
		 * use the information in captured packet to reconstruct new hello packet to send
		 */
		try {
			/**
			 * ip header
			 */
			ddpack.setIPv4Parameter(pack.priority,pack.d_flag,pack.t_flag,pack.r_flag,pack.rsv_tos,pack.rsv_frag
					,pack.dont_frag,pack.more_frag,pack.offset,pack.ident+1,1,packet_factory.OSPF_PAKET,
					InetAddress.getLocalHost(),pack.src_ip);
			/**
			 * ospf header
			 */
			
			
			byte[] sr=new byte[4];
			for(int i=0;i<4;i++){
				sr[i]=((OSPF_packet)pack).source_router[i];
			}
			sr[3]+=1;
			byte[] aid={0x00,0x00,0x00,0x00};
			byte[] authData={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			ddpack.setAllOSPFHeader(2, 2, 0, sr, aid, 0, authData);
			/**
			 * ospf DD content
			 */
			ddpack.setInterfaceMTU(1500);
			ddpack.setOptions((byte)0x52);
			ddpack.setDDDescription(7);
//			ddpack.setSequence(((OSPF_DD_Packet)pack).DD_sequence);
			ddpack.setSequence(2222);
		//	byte[] lsaData = {0x00,0x20,0x22,0x01,0x01,0x01,0x03,0x01,0x01,0x01,0x03,0x01,(byte) 0x80,0x00,0x00,0x02,0x43,(byte) 0xb5,0x00,0x30};
		//	ddpack.setLSAData(lsaData, lsaData.length);
			ddpack.data=ddpack.getSendableData();
			/**
			 * set datalink header
			 */
			EthernetPacket ether=new EthernetPacket();
			ether.frametype=EthernetPacket.ETHERTYPE_IP;
			ether.src_mac=jpcap_util.getMyMac();
			ether.dst_mac=dstMac;
			//ether.dst_mac=dst;
			ddpack.datalink=ether;
		}catch(Exception e ){
			e.printStackTrace();
		}
		return ddpack;
	}
	/**
	 * 用于自身routerid 小于对面承认对面master时
	 * @param pack
	 * @return
	 */
	public static OSPF_DD_Packet getAnwserDD(IPPacket pack){
		OSPF_DD_Packet ddpack=new OSPF_DD_Packet();
//		byte dstIP[]={(byte) 0xE0,(byte)0x00,(byte)0x00,(byte)0x05};//broadcast dst 224.0.0.5
		byte dstMAC[]=new byte[6];//ipv4mcast
		
		for(int i=0;i<6;i++){
			dstMAC[i]=pack.header[6+i];
		}
		/**
		 * use the information in captured packet to reconstruct new hello packet to send
		 */
		try {
			/**
			 * ip header
			 */
			ddpack.setIPv4Parameter(pack.priority,pack.d_flag,pack.t_flag,pack.r_flag,pack.rsv_tos,pack.rsv_frag
					,pack.dont_frag,pack.more_frag,pack.offset,pack.ident,1,packet_factory.OSPF_PAKET,
					InetAddress.getLocalHost(),pack.src_ip);
			/**
			 * ospf header
			 */
//			byte[] sr=new byte[4];
//			for(int i=0;i<4;i++){
//				sr[i]=((OSPF_packet)pack).source_router[i];
//			}
//			sr[3]+=1;
			byte[] sr={(byte) 0xfe,(byte) 0xfe,(byte) 0xfe,(byte) 0xfe};
 			byte[] aid={0x00,0x00,0x00,0x00};
 			for(int i=0;i<4;i++){
 				aid[i]=((OSPF_packet)pack).area_id[i];
			}
			byte[] authData={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			ddpack.setAllOSPFHeader(2, 2, 0, sr, aid, 0, authData);
			/**
			 * ospf DD content
			 */
			ddpack.setInterfaceMTU(util.util.parseByte2Short(((OSPF_DD_Packet)pack).interface_MTU));
			ddpack.setOptions((byte)0x42);
			if(((OSPF_DD_Packet)pack).DB_description>3){
				ddpack.setDDDescription(7);
				ddpack.setSequence(1234);
			}
			else{
				ddpack.setDDDescription(1);
				ddpack.setSequence(1235);
			}
//			byte[] lsaData = {0x00,0x20,0x22,0x01,0x01,0x01,0x03,0x01,0x01,0x01,0x03,0x01,(byte) 0x80,0x00,0x00,0x02,0x43,(byte) 0xb5,0x00,0x30};
//			ddpack.setLSAData(lsaData, lsaData.length);
			ddpack.data=ddpack.getSendableData();
			/**
			 * set datalink header
			 */
			EthernetPacket ether=new EthernetPacket();
			ether.frametype=EthernetPacket.ETHERTYPE_IP;
			ether.src_mac=jpcap_util.getMyMac();
			ether.dst_mac=dstMAC;
			//ether.dst_mac=dst;
			ddpack.datalink=ether;
		}catch(Exception e ){
			e.printStackTrace();
		}
		return ddpack;
	}
	public static OSPF_LSU_Packet getAnwserLSU(IPPacket pack){
		OSPF_LSU_Packet LSUpack=new OSPF_LSU_Packet();
		byte dstMAC[]=new byte[6];//ipv4mcast
		
		for(int i=0;i<6;i++){
			dstMAC[i]=pack.header[6+i];
		}
		/**
		 * use the information in captured packet to reconstruct new hello packet to send
		 */
		try {
			/**
			 * ip header
			 */
			LSUpack.setIPv4Parameter(pack.priority,pack.d_flag,pack.t_flag,pack.r_flag,pack.rsv_tos,pack.rsv_frag
					,pack.dont_frag,pack.more_frag,pack.offset,pack.ident,1,packet_factory.OSPF_PAKET,
					InetAddress.getLocalHost(),pack.src_ip);
			/**
			 * ospf header
			 */
//			byte[] sr=new byte[4];
//			for(int i=0;i<4;i++){
//				sr[i]=((OSPF_packet)pack).source_router[i];
//			}
//			sr[3]+=1;
			byte[] sr={(byte) 0xfe,(byte) 0xfe,(byte) 0xfe,(byte) 0xfe};
 			byte[] aid={0x00,0x00,0x00,0x00};
 			for(int i=0;i<4;i++){
 				aid[i]=((OSPF_packet)pack).area_id[i];
			}
			byte[] authData={0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
			LSUpack.setAllOSPFHeader(2, 5, 0, sr, aid, 0, authData);
			/**
			 * ospf LSU content
			 */
			LSUpack.LSAs_data=new byte[2];
//			byte[] lsaData = {0x00,0x20,0x22,0x01,0x01,0x01,0x03,0x01,0x01,0x01,0x03,0x01,(byte) 0x80,0x00,0x00,0x02,0x43,(byte) 0xb5,0x00,0x30};
//			ddpack.setLSAData(lsaData, lsaData.length);
			LSUpack.data=LSUpack.getSendableData();
			/**
			 * set datalink header
			 */
			EthernetPacket ether=new EthernetPacket();
			ether.frametype=EthernetPacket.ETHERTYPE_IP;
			ether.src_mac=jpcap_util.getMyMac();
			ether.dst_mac=dstMAC;
			//ether.dst_mac=dst;
			LSUpack.datalink=ether;
		}catch(Exception e ){
			e.printStackTrace();
		}
		return LSUpack;
	}
	public static boolean isSendByMe(Packet p){
		try {
			if(((IPPacket) p).src_ip.equals(InetAddress.getLocalHost())){//filter packets send from localhost
				return true;
			}
			if(((IPPacket) p).src_ip.equals(InetAddress.getByName("1.1.2.2"))){
				return true;
			}
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
	public static OSPF_LSack_Packet getLSAck(IPPacket pack) {
		// TODO Auto-generated method stub
		return null;
	}
}
