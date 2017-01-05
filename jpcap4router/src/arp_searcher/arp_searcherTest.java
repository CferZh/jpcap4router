package arp_searcher;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.packet.ARPPacket;
import jpcapUtil.JpcapUtil;

public class arp_searcherTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildARPBroadcastPack() {
		JpcapSender sender=null;
		NetworkInterface[] devices = JpcapCaptor.getDeviceList(); 
		try {
			sender= JpcapSender.openDevice(devices[1]);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ARPPacket packet=arp_searcher.buildARPBroadcastPack("10.203.8.129","10.203.8.148");
		for(int i=0 ; i<10;i++)
			sender.sendPacket(packet);
		fail("Not yet implemented");
	}

}
