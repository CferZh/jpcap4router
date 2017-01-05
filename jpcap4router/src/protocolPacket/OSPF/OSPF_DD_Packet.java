package protocolPacket.OSPF;

import java.lang.reflect.InvocationTargetException;

import jpcap.packet.IPPacket;
import protocolPacket.OSPFPacket;

public class OSPF_DD_Packet extends OSPFPacket {
	/**
	 * variables in OSPF HELLO packet
	 */
	private static final int NORMAL_DD_LEN=8;
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
	public OSPF_DD_Packet() {
		// TODO Auto-generated constructor stub
		super();
	}
	public void setInterfaceMTU(int mtu){
		interface_MTU[0]=(byte)(mtu>>8&0xff);
		interface_MTU[1]=(byte)(mtu&0xff);
	}
	/**
	 * set value of options
	 * @param DN_flag
	 * @param O_flag
	 * @param DC_flag demand circuits   This bit describes the router's handling of demand circuits, as
        specified in [Ref21]
	 * @param L_flag   lls data block This bit describes the router's willingness to receive and
        forward External-Attributes-LSAs, as specified in [Ref20]
	 * @param NP_flag   This bit describes the handling of Type-7 LSAs, as specified in
        [Ref19]
	 * @param MC_flag  multicast This bit describes whether IP multicast datagrams are forwarded
        according to the specifications in [Ref18]
	 * @param E_flag   external routing   This bit describes the way AS-external-LSAs are flooded
	 * @param MT_flag  multi topology
	 */
	public void setOptions(boolean DN_flag,boolean O_flag,boolean DC_flag,boolean L_flag,boolean NP_flag,boolean MC_flag,boolean E_flag,boolean MT_flag){
		byte opt=0;
		if(DN_flag)
			opt+=0x80;
		if(O_flag)
			opt+=0x40;
		if(DC_flag)
			opt+=0x20;
		if(L_flag)
			opt+=0x10;
		if(NP_flag)
			opt+=0x08;
		if(MC_flag)
			opt+=0x04;
		if(E_flag)
			opt+=0x02;
		if(MT_flag)
			opt+=0x01;
		options=opt;
	}
	/**
	 * set value of options by a byte
	 * @param _option
	 */
	public void setOptions(byte _option){
		options=_option;
	}
	public void setDDDescription(boolean rFlag,boolean iflag,boolean mflag,boolean msflag){
		byte opt=0;
		if(rFlag){
			opt+=0x08;
		}
		if(iflag){
			opt+=0x04;
		}
		if(mflag){
			opt+=0x02;
		}
		if(msflag){
			opt+=0x01;
		}
		DB_description=opt;
	}
	public void setDDDescription(int opt){
		DB_description=(byte)(opt&0xff);
	}
	public void setSequence(byte[] seq){
		for(int i=0;i<4;i++){
			DD_sequence[i]=seq[i];
		}
	}
	public void setSequence(int seq){
		for(int i=0;i<4;i++){
			int offset=(3-i)*8;
			DD_sequence[i]=(byte)(seq>>offset&0xff);
		}
	}
	
	public byte[] getSendableData(){
		
		byte[] data=(LSA_data==null)?new byte[OSPF_HEADER_LEN+NORMAL_DD_LEN+12]:new byte[OSPF_HEADER_LEN+NORMAL_DD_LEN+LSA_data.length+12];
		byte[] ospfheader=getOSPFHeaderData();
		for(int i=0;i<OSPF_HEADER_LEN;i++){// set content of ospf header , there is auth data && checksum tobe set
			data[i]=ospfheader[i];
		}
		data[OSPF_HEADER_LEN]=interface_MTU[0];
		data[OSPF_HEADER_LEN+1]=interface_MTU[1];
		data[OSPF_HEADER_LEN+2]=options;
		data[OSPF_HEADER_LEN+3]=DB_description;
		for(int i=0;i<4;i++){
			data[OSPF_HEADER_LEN+4+i]=DD_sequence[i];
		}
		if(LSA_data!=null){
			for(int i=0;i<LSA_data.length;i++){
				data[OSPF_HEADER_LEN+8+i]=LSA_data[i];
			}
		}
		data[2]=(byte)((data.length-12)>>8&0xff);
		data[3]=(byte)((data.length-12)&0xff);
		byte[] pchecksum=util.util.getchecksum(data);
		data[12]=pchecksum[0];
		data[13]=pchecksum[1];
		byte[] lldata={(byte)0xff,(byte)0xf6,0x00,0x03,0x00,0x01,0x00,0x04,0x00,0x00,0x00,0x01};
		int offset=OSPF_HEADER_LEN+NORMAL_DD_LEN+(LSA_data==null?0:LSA_data.length);
		for(int i=0;i<12;i++){
			data[offset+i]=lldata[i];
		}
		return data;
	}
	public void setLSAData(byte[] lsas,int len){
		LSA_data=new byte[len];
		for(int i=0;i<len;i++){
			LSA_data[i]=lsas[i];
		}
	}
	
}
