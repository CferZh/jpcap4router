package statMachine;

import jpcapUtil.JpcapUtil;
import packetTool.PacketTool;
import protocolPacket.OSPF.OSPF_DD_Packet;
import protocolPacket.OSPF.OSPF_Hello_Packet;
import protocolPacket.OSPF.OSPF_LSU_Packet;

public class NormalOSPFMachine extends OSPFStatMachine {

	@Override
	public void dealwithHello(OSPF_Hello_Packet pack) {
		// TODO Auto-generated method stub
		//回应hello,建立邻居
		OSPF_Hello_Packet hello=null;
		hello=PacketTool.getAnswerHello(pack);
		if(hello!=null){
			JpcapUtil utilInstance=JpcapUtil.getInstance();
			utilInstance.sendPacket(hello);
			System.out.println("send a response hello pack");
		}
		return;
	}

	@Override
	public void dealwithDB(OSPF_DD_Packet pack) {
		// TODO Auto-generated method stub
		OSPF_DD_Packet ddpack=null;
		ddpack=PacketTool.getAnwserDD(pack);
		if(ddpack!=null){
			JpcapUtil utilInstance=JpcapUtil.getInstance();
			utilInstance.sendPacket(ddpack);
		}
		return;
	}
	@Override
	public void dealwithLSU(OSPF_LSU_Packet pack) {
		// TODO Auto-generated method stub
		
	}

}
