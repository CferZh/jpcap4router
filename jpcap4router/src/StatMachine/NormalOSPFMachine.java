package StatMachine;

import jpcap_util.jpcap_util;
import packetTool.packetTool;
import protocol_packet.OSPF.OSPF_DD_Packet;
import protocol_packet.OSPF.OSPF_Hello_Packet;
import protocol_packet.OSPF.OSPF_LSU_Packet;

public class NormalOSPFMachine extends OSPFStatMachine {

	@Override
	public void dealwithHello(OSPF_Hello_Packet pack) {
		// TODO Auto-generated method stub
		//回应hello,建立邻居
		OSPF_Hello_Packet hello=null;
		hello=packetTool.getAnswerHello(pack);
		if(hello!=null){
			jpcap_util utilInstance=jpcap_util.getInstance();
			utilInstance.sendPacket(hello);
			System.out.println("send a response hello pack");
		}
		return;
	}

	@Override
	public void dealwithDB(OSPF_DD_Packet pack) {
		// TODO Auto-generated method stub
		OSPF_DD_Packet ddpack=null;
		ddpack=packetTool.getAnwserDD(pack);
		if(ddpack!=null){
			jpcap_util utilInstance=jpcap_util.getInstance();
			utilInstance.sendPacket(ddpack);
		}
		return;
	}
	@Override
	public void dealwithLSU(OSPF_LSU_Packet pack) {
		// TODO Auto-generated method stub
		
	}

}
