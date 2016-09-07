package routerCapture;

import javax.lang.model.element.PackageElement;

import jpcap.JpcapCaptor;
import jpcap_util.callback_reciever;
import jpcap_util.jpcap_util;
import packetTool.packetTool;
import protocol_packet.OSPF.OSPF_Hello_Packet;

public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		 * get incetance of jpcapCaptor
		 * argv: -1 choose netcard
		 * 		 >0 using assigned netcardID
		 * 		on zyf pc 1 means wlan0 3 means reltek
		 * 		on zyf_work_pc 1means wlan
		 */
		jpcap_util jpcap=jpcap_util.getInstance(1);//<0 means choose netdevice ; >=1 means netdevice id 
		//start listen with the callback_receiver provided by argv2
		jpcap.startCaptorWithReciever(new callback_reciever());
//		byte[] netmask={(byte) 0xff,(byte) 0xff,(byte) 0xfc,0x00};
//		OSPF_Hello_Packet hello=packetTool.getBroadcastHello(netmask);
//		jpcap_util utilInstance=jpcap_util.getInstance(1);
//		utilInstance.sendPacket(hello);
		System.out.println("wait");
	}

}
