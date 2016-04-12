package protocol_packet.OSPF;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF_packet;

public class OSPF_LSack_Packet extends OSPF_packet {
	/**
	 * variables in OSPF HELLO packet
	 */
	
	public byte[] lsa_data;
	public OSPF_LSack_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
		super(p);
	}
	
	
}
