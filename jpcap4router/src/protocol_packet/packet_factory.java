package protocol_packet;

import jpcap.packet.IPPacket;

public class packet_factory {
//	private final static int PROTOCOL_BYTE=9;//ip packet 10th byte declear the type of packet
	private final static int OSPF_PAKET=0x59;
	public static IPPacket createPacket(IPPacket p){
		switch(p.protocol){
		case OSPF_PAKET://OSPF
			return OSPF_packet.analyzePacket(p);
		default:
			//System.out.println("unmatched protocol packet");  // make comment when test incase too many log
			return null;
		}
	}
}
