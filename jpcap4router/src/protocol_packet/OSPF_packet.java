package protocol_packet;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF.OSPF_Hello_Packet;

public class OSPF_packet extends IPPacket {
	
	/**
	 * final variables
	 */
	protected static final int OSPF_TYPE_BYTE=1;//OSPF header 2nd byte declear the type of packet
	protected static final int OSPF_HELLO=0x01;
	protected static final int OSPF_DD=0x02;
	protected static final int OSPF_LSR=0x03;
	protected static final int OSPF_LSU=0x04;
	protected static final int OSPF_LSack=0x05;
	protected static final int OSPF_HEADER_LEN=24;//24 byte ospf header
	/**
	 * variables in ospf header
	 */
	public byte version=0;//1 byte in packet using hexadcimal
	public byte message_type=0;//1 byte in packet using hexadcimal
	public byte[] packet_length=new byte[2];//2 byte in packet using hexadcimal
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
	public OSPF_packet(IPPacket p){
		version=p.data[0];
		message_type=p.data[1];
		packet_length[0]=p.data[2];
		packet_length[1]=p.data[3];
		for (int i = 0; i < 4; i++) {// 4 byte source_router
			source_router[i] = p.data[4+i];
		}
		for (int i = 0; i < 4; i++) {// 4 byte area_id
			area_id[i] = p.data[8+i];
		}
		checksum[0]=p.data[12];
		checksum[1]=p.data[13];
		auth_type[0]=p.data[14];
		auth_type[1]=p.data[15];
		for(int i=0 ;i<8 ;i++){//8 byte auth data
			auth_data[i]=p.data[16+i];
		}
	}
	public OSPF_packet() {
		// TODO Auto-generated constructor stub
		super();
	}
	public int getPacketLen(){
		return (packet_length[0]&0xFF)<<8 + (packet_length[1]&0xff);
	}
	/**
	 * combine all value in packet to a byte[] to append in ippackt.data
	 * TODO simple password auth && MD5 auth , now null auth only
	 * @return
	 */
	public byte[] getOSPFHeaderData(){
		byte[] data=new byte[OSPF_HEADER_LEN];
		data[0]=version;
		data[1]=message_type;
		data[2]=packet_length[0];
		data[3]=packet_length[1];
		for(int i=0 ; i< 4 ;i++){
			data[4+i]=source_router[i];
		}
		for(int i =0 ;i<4 ;i++){
			data[8+i]=area_id[i];
		}
		data[12]=0;//checksum 2 byte
		data[13]=0;//checksum 
		data[14]=auth_data[0];
		data[15]=auth_data[1];
		for(int i=0;i<8;i++){//only when null authentication
			data[16+i]=0;
		}
		return data;
	}
	public void setVersion(int _version){
		version=(byte)(_version&0xff);
	}
	public void setMessageType(int _type){
		message_type=(byte)(_type&0xff);
	}
	public void setPackLen(int len){
		packet_length[0]=(byte)(len>>8&0xff);
		packet_length[1]=(byte)(len&0xff);
	}
	public void setSourceRouter(byte[] sr){
		for(int i=0;i<4;i++){
			source_router[i]=sr[i];
		}
	}
	public void setAreaID(byte[] aid){
		for(int i=0;i<4;i++){
			area_id[i]=aid[i];
		}
	}
	public void initChecksum(){
		checksum[0]=0;
		checksum[1]=0;
	}
	public void setChecksum(byte[] check){
		check[0]=check[0];
		check[1]=check[1];
	}
	public void setAuthType(int type){
		auth_type[0]=(byte)(type>>8&0xff);
		auth_type[1]=(byte)(type&0xff);
	}
	public void setAuthData(byte[] data){
		for(int i=0;i<4;i++){
			auth_data[i]=data[i];
		}
	}
	public void setAll(int vers,int msgType,int packLen,byte[] srcRouter,byte[] areaID,int authType,byte[] authData){
		setVersion(vers);
		setMessageType(msgType);
		setPackLen(packLen);
		setSourceRouter(srcRouter);
		setAreaID(areaID);
		initChecksum();
		setAuthType(authType);
		setAuthData(authData);
	}
}
