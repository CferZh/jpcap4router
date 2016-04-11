package protocol_packet.OSPF;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF_packet;

public class OSPF_Hello_Packet extends OSPF_packet {
	/**
	 * variables in OSPF HELLO packet
	 */
	
	public byte[] net_mask=new byte[4];
	public byte[] hello_interval=new byte[2];
	public byte options=0;
	public byte router_priority=0;
	public byte[] router_dead_interval=new byte[4];
	public byte[] designed_router=new byte[4];
	public byte[] bk_designed_router=new byte[4];
	public byte[] lls_data=new byte[12];
	public byte[] active_neighbor;
	public OSPF_Hello_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
	}
	
	
}
