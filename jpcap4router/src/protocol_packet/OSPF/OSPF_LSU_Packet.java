package protocol_packet.OSPF;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF_packet;

public class OSPF_LSU_Packet extends OSPF_packet {
	/**
	 * variables in OSPF HELLO packet
	 */
	
	public byte[] LSAs_num=new byte[4];
	public byte[] LSAs_data;
	
	public OSPF_LSU_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
	}
	
	
}
