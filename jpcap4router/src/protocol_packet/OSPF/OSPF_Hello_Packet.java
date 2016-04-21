package protocol_packet.OSPF;

import jpcap.packet.IPPacket;
import protocol_packet.OSPF_packet;

public class OSPF_Hello_Packet extends OSPF_packet {
	
	protected static final int NORMAL_HELLO_LEN=20;//hello packet length except active neighbor
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
	public byte[] lls_data=new byte[12];//cisco only , set options L_flag to 1 when this present, 
	public byte[] active_neighbor=null;
	
	private byte send_check=0;//check all value in ospf protocol have been set before send
	
	public OSPF_Hello_Packet(){
		super();
	}
	/**
	 * analyze ip packet to ospf hello packet
	 * @param p
	 */
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
	/**
	 * TODO finish the check before send ensure all the value have been set
	 * @return
	 */
	public int checkBeforeSend(){
		return 0;
	}
	/**
	 *  set the value of netmask
	 * @param mask
	 * @throws Exception
	 */
	public void setNetMask(byte[] mask) {
		if(mask.length!=4){
			System.out.println("netmask length error");
			return;
		}
		for(int i=0;i<4;i++){
			net_mask[i]=mask[i];
		}
	}
	/**
	 * set value of hellointerval
	 * @param interval
	 */
	public void setHelloInterval(int interval){
		hello_interval[0]=(byte)((interval>>8)&0xff);
		hello_interval[1]=(byte)(interval&0xff);
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
	/**
	 * set router priority
	 * @param priority
	 */
	public void setRouterPriority(byte priority){
		router_priority=priority;
	}
	public void setRouterDeadInterval(int interval){
		router_dead_interval[0]=(byte)((interval>>24)&0xff);
		router_dead_interval[1]=(byte)((interval>>16)&0xff);
		router_dead_interval[2]=(byte)((interval>>8)&0xff);
		router_dead_interval[3]=(byte)(interval&0xff);
	}
	public void setDesignedRouter(byte[] router) {
		if(router.length!=4){
			System.out.println("designed router length error!");
			return;
		}
		for(int i=0;i<4;i++){
			designed_router[i]=router[i];
		}
	}
	
	public void setBKDesignedRouter(byte[] bkrouter) {
		if(bkrouter.length!=4){
			System.out.println("back up designed router length error!");
			return;
		}
		for(int i=0;i<4;i++){
			bk_designed_router[i]=bkrouter[i];
		}  
	}
	public void setActiveNeighbor(byte[] neighbors){
		active_neighbor=new byte[neighbors.length];
		for(int i=0;i<neighbors.length;i++){
			active_neighbor[i]=neighbors[i];
		}
	}
	/**
	 * combine values in packet to a byte[] in order to append to ippacket.data when packet send
	 * TODO simple password auth && md5 auth XXX
	 * @return
	 */
	public byte[] getSendableData(){
		if(checkBeforeSend()>0){
			System.out.println("still values not set in hello packet");
			return null;
		}
		byte[] data=(active_neighbor==null)?new byte[OSPF_HEADER_LEN+NORMAL_HELLO_LEN+12]:new byte[OSPF_HEADER_LEN+NORMAL_HELLO_LEN+active_neighbor.length+12];
		byte[] ospfheader=getOSPFHeaderData();
		for(int i=0;i<OSPF_HEADER_LEN;i++){// set content of ospf header , there is auth data && checksum tobe set
			data[i]=ospfheader[i];
		}
		for(int i=0;i<4;i++){
			data[OSPF_HEADER_LEN+i]=net_mask[i];
		}
		data[OSPF_HEADER_LEN+4]=hello_interval[0];
		data[OSPF_HEADER_LEN+5]=hello_interval[1];
		data[OSPF_HEADER_LEN+6]=options;
		data[OSPF_HEADER_LEN+7]=router_priority;
		for(int i=0;i<4;i++){
			data[OSPF_HEADER_LEN+8+i]=router_dead_interval[i];
		}
		for(int i=0;i<4;i++){
			data[OSPF_HEADER_LEN+12+i]=designed_router[i];
		}
		for(int i=0;i<4;i++){
			data[OSPF_HEADER_LEN+16+i]=bk_designed_router[i];
		}
		if(active_neighbor!=null){
			for(int i=0;i<active_neighbor.length;i++){
				data[OSPF_HEADER_LEN+20+i]=active_neighbor[i];
			}
		}
		byte[] pchecksum=util.util.getchecksum(data);
		data[12]=pchecksum[0];
		data[13]=pchecksum[1];
		byte[] lldata={(byte)0xff,(byte)0xf6,0x00,0x03,0x00,0x01,0x00,0x04,0x00,0x00,0x00,0x01};
		for(int i=0;i<12;i++){
			data[OSPF_HEADER_LEN+20+active_neighbor.length+i]=lldata[i];
		}
		return data;
	}
	
}
