package protocolPacket.OSPF;

import jpcap.packet.IPPacket;
import protocolPacket.OSPFPacket;

public class OSPF_LSR_Packet extends OSPFPacket {
	/**
	 * variables in OSPF HELLO packet
	 */
	
	public byte[] LSR_data;
	
	public OSPF_LSR_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
		super(p);
	}
	
	class LSR_section{
		public byte[] ls_type=new byte[4];
		public byte[] link_state_id=new byte[4];
		public byte[] advertising_router=new byte[4];
	}
}
