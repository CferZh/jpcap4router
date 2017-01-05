package protocolPacket.OSPF;

import jpcap.packet.IPPacket;
import protocolPacket.OSPFPacket;

public class OSPF_LSack_Packet extends OSPFPacket {
	/**
	 * variables in OSPF lsack packet
	 */
	
	public byte[] lsa_data=null;
	public OSPF_LSack_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
		super(p);
	}
	
	public byte[] getSendableData(){
		byte[] data=null;
		data=new byte[OSPF_HEADER_LEN+lsa_data.length];
		data[0]=version;
		data[1]=message_type;
		data[2]=packet_length[0];
		data[3]=packet_length[1];
		for(int i=0;i<3;i++){
			data[4+i]=source_router[i];
		}
		for(int i=0;i<3;i++){
			data[8+i]=area_id[i];
		}
		data[12]=0;
		data[13]=0;//checksum
		for(int i=0;i<10;i++){//auth type & auth data
			data[14+i]=0;
		}
		for(int i=0;i<lsa_data.length;i++){
			data[OSPF_HEADER_LEN+i]=lsa_data[i];
		}
		byte[] _checksum=util.util.getchecksum(data);
		data[12]=_checksum[0];
		data[13]=_checksum[1];
		return data;
	}
}
