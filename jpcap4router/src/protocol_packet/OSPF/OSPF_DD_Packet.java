package protocol_packet.OSPF;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF_packet;

public class OSPF_DD_Packet extends OSPF_packet {
	/**
	 * variables in OSPF HELLO packet
	 */
	
	public byte[] interface_MTU=new byte[2];
	public byte options=0;
	public byte DB_description=0;//half byte actually
	public byte[] DD_sequence=new byte[4];
	public byte[] lls_data=new byte[12];//12 byte if there is not auth
	public byte[] LSA_data;
	public OSPF_DD_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
		super(p);
	}
	
	
}
