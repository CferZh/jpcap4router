package statMachine;

import jpcap.PacketReceiver;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcapUtil.JpcapUtil;
import packetTool.PacketTool;
import protocolPacket.PacketFactory;
import protocolPacket.OSPF.OSPF_DD_Packet;
import protocolPacket.OSPF.OSPF_Hello_Packet;
import protocolPacket.OSPF.OSPF_LSU_Packet;
import protocolPacket.OSPF.OSPF_LSack_Packet;

public abstract class OSPFStatMachine   {
	JpcapUtil jpcap=null;
	public void startMachine(){
		//收一个包 
		//判断是否为OSPF
		//判断OSPF包类型 然后分别  dealWithHELLO dealWithDD dealWithLSU 
		jpcap=JpcapUtil.getInstance();
		jpcap.startCaptorWithReciever(new OSPFreciver());
		
	}
	public void stopMachine(){
		if(jpcap!=null){
			jpcap.stopCaptor();
		}
	}

	public abstract void dealwithHello(OSPF_Hello_Packet pack);
	public abstract void dealwithDB(OSPF_DD_Packet pack);
	public abstract void dealwithLSU(OSPF_LSU_Packet pack);

	public class OSPFreciver implements PacketReceiver{

		@Override
		public void receivePacket(Packet p) {
			// TODO Auto-generated method stub
			//IP packet only
			if(p instanceof IPPacket){
				if(PacketTool.isSendByMe(p)){//filter packet sent by localhost
					return;
				}
				IPPacket pack=PacketFactory.createPacket((IPPacket) p);
				if(pack instanceof OSPF_Hello_Packet){//ospf hello -> need to return a suitable hello to build neighor
					dealwithHello((OSPF_Hello_Packet) pack);
					return;
				}
				if(pack instanceof OSPF_DD_Packet){//may be exstart need to return a suitable DD to establish master/slave relation
					dealwithDB((OSPF_DD_Packet) pack);
					return;
				}
				if(pack instanceof OSPF_LSU_Packet){//need to answer lsack
					dealwithLSU((OSPF_LSU_Packet) pack);
					return;
				}	
			}
		}
		
	}
}
