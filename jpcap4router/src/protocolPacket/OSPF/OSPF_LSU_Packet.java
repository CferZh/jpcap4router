package protocolPacket.OSPF;

import jpcap.packet.IPPacket;
import protocolPacket.OSPFPacket;

public class OSPF_LSU_Packet extends OSPFPacket {
	/**
	 * variables in OSPF LSU pack
	 */
	
	public byte[] LSAs_num=new byte[4];
	public byte[] LSAs_data=null;
	public OSPF_LSU_Packet() {
		// TODO Auto-generated constructor stub	
		super();
	}
	public OSPF_LSU_Packet(IPPacket p) {
		// TODO Auto-generated constructor stub	
		super(p);
	}
	public void setLSAnum(int num){
		for(int i=0;i<4;i++){
			LSAs_num[i]=(byte) ((num>>(24-3*i))&0xff);
		}
	}
	public byte[] getSendableData(){
		
		byte[] data=(LSAs_data==null)?new byte[OSPF_HEADER_LEN+4+12]:new byte[OSPF_HEADER_LEN+LSAs_data.length+4+12];
		byte[] ospfheader=getOSPFHeaderData();
		for(int i=0;i<OSPF_HEADER_LEN;i++){// set content of ospf header , there is auth data && checksum tobe set
			data[i]=ospfheader[i];
		}
		for(int i=0;i<4;i++)
			data[OSPF_HEADER_LEN]=LSAs_num[i];
		if(LSAs_data!=null){
			for(int i=0;i<LSAs_data.length;i++)
			data[OSPF_HEADER_LEN+4+i]=LSAs_data[i];
		}
		data[2]=(byte)((data.length-12)>>8&0xff);
		data[3]=(byte)((data.length-12)&0xff);
		byte[] pchecksum=util.util.getchecksum(data);
		data[12]=pchecksum[0];
		data[13]=pchecksum[1];
		byte[] lldata={(byte)0xff,(byte)0xf6,0x00,0x03,0x00,0x01,0x00,0x04,0x00,0x00,0x00,0x01};
		int offset=OSPF_HEADER_LEN+4+LSAs_data.length;
		for(int i=0;i<12;i++){
			data[offset+i]=lldata[i];
		}
		return data;
	}
	
}
