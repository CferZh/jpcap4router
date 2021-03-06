package protocolPacket;

import jpcap.packet.IPPacket;

public class PacketFactory {
//	private final static int PROTOCOL_BYTE=9;//ip packet 10th byte declear the type of packet
	public final static byte OSPF_PAKET=0x59;//0x59
	public static IPPacket createPacket(IPPacket p){
		switch(p.protocol){
		case OSPF_PAKET://OSPF
			return OSPFPacket.analyzePacket(p);
		default:
			//System.out.println("unmatched protocol packet");  // make comment when test incase too many log
			return null;
		}
	}
}
