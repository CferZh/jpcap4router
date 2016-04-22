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
		interface_MTU[0]=p.data[OSPF_HEADER_LEN];
		interface_MTU[1]=p.data[OSPF_HEADER_LEN+1];
		options=p.data[OSPF_HEADER_LEN+2];
		DB_description=p.data[OSPF_HEADER_LEN+3];
		for(int i=0;i<4;i++){
			DD_sequence[i]=p.data[OSPF_HEADER_LEN+4+i];
		}
		for(int i=0;i<12;i++){
			lls_data[i]=p.data[OSPF_HEADER_LEN+8+i];
		}
	}
	
	
}
