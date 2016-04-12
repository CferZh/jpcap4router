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
		//TODO lls_data section & active neighbor section
		super(p);
		for(int i=0;i<4 ;i++){//4 byte net mask
			net_mask[i]=p.data[24+i];
		}
		hello_interval[0]=p.data[28];
		hello_interval[1]=p.data[29];
		options=p.data[30];
		router_priority=p.data[31];
		for(int i=0;i<4;i++){//4byte router dead interval
			router_dead_interval[i]=p.data[32+i];			
		}
		for(int i=0;i<4;i++){
			designed_router[i]=p.data[36+i];
		}
		for (int i = 0; i < 4; i++) {
			bk_designed_router[i]=p.data[40+i];
		}
		
	}
	
	
}
