package protocol_packet;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF.OSPF_Hello_Packet;

public class OSPF_packet extends IPPacket {
	
	/**
	 * final variables
	 */
	private static final int OSPF_TYPE_BYTE=1;//OSPF header 2nd byte declear the type of packet
	private static final int OSPF_HELLO=0x01;
	private static final int OSPF_DD=0x02;
	private static final int OSPF_LSR=0x03;
	private static final int OSPF_LSU=0x04;
	private static final int OSPF_LSack=0x05;
	/**
	 * variables in ospf header
	 */
	public byte version=0;//1 byte in packet using hexadcimal
	public byte message_type=0;//1 byte in packet using hexadcimal
	public int packet_length=0;//2 byte in packet using hexadcimal
	public byte[] source_router=new byte[4]; //4 byte in packet using hexadcimal 0x01 0x01 0x01 0x01 means 1.1.1.1
	public byte[] area_id=new byte[4];//4 byte in packet using hexadcimal 0x01 0x01 0x01 0x01 means 1.1.1.1
	public byte[] checksum=new byte[2];//2 byte in packet
	public byte[] auth_type=new byte[2];//2 byte in packet using hexadcimal
	public byte[] auth_data=new byte[8];//8 byte in packet
	/**
	 * create specific OSPF protocol pcaket
	 * @param p the whole ip packet
	 * @return specific OSPF in (HELLO,DD,LSR,LSU,LSAck)
	 */
	public static OSPF_packet analyzePacket(IPPacket p){
		
		switch(p.data[OSPF_TYPE_BYTE]){
		case OSPF_HELLO:
			return new OSPF_Hello_Packet(p); 
		
		
		
		default:return null;
		}
	}
}
