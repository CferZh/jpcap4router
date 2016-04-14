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
	
	private byte send_check=0;//check all value in ospf protocol have been set before send
	
	public OSPF_Hello_Packet(){
		super();
	}
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
	
	public int checkBeforeSend(){
		return 0;
	}
	
	public void setNetMask(byte[] mask) throws Exception{
		if(mask.length!=4){
			throw new Exception("netmask length error!");
		}
		for(int i=0;i<4;i++){
			net_mask[i]=mask[i];
		}
	}
	public void setHelloInterval(int interval){
		hello_interval[0]=(byte)((interval>>8)&0xff);
		hello_interval[1]=(byte)(interval&0xff);
	}
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
	public void setOptions(byte _option){
		options=_option;
	}
	public void setRouterPriority(byte priority){
		router_priority=priority;
	}
	public void setRouterDeadInterval(int interval){
		router_dead_interval[0]=(byte)((interval>>24)&0xff);
		router_dead_interval[1]=(byte)((interval>>16)&0xff);
		router_dead_interval[2]=(byte)((interval>>8)&0xff);
		router_dead_interval[3]=(byte)(interval&0xff);
	}
	public void setDesignedRouter(byte[] router) throws Exception{
		if(router.length!=4){
			throw new Exception("designed router length error!");
		}
		for(int i=0;i<4;i++){
			designed_router[i]=router[i];
		}
	}
	
	public void setBKDesignedRouter(byte[] bkrouter) throws Exception{
		if(bkrouter.length!=4){
			throw new Exception("back up designed router length error!");
		}
		  
	}
	public void setActiveNeighbor(byte[] neighbors){
		active_neighbor=new byte[neighbors.length];
		for(int i=0;i<neighbors.length;i++){
			active_neighbor[i]=neighbors[i];
		}
	}
}
