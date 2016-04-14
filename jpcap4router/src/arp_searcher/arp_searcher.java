package arp_searcher;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.Packet;

public class arp_searcher {
	public static ARPPacket buildARPBroadcastPack(String ip,String myip){
		ARPPacket arp=new ARPPacket();
		InetAddress broad_ip=null;
		
		try {
			broad_ip = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("this is brod ip"+broad_ip);
		byte[] broad_mac=stomac("FF-FF-FF-FF-FF-FF");
		arp.hardtype = ARPPacket.HARDTYPE_ETHER;
		arp.prototype = ARPPacket.PROTOTYPE_IP;
		arp.operation = ARPPacket.ARP_REQUEST;
		arp.hlen = 6;
		arp.plen = 4;
		arp.sender_hardaddr = stomac("74-E5-0B-45-82-7A");
		try {
			arp.sender_protoaddr = InetAddress.getByName(myip).getAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arp.target_hardaddr = broad_mac;
		arp.target_protoaddr = broad_ip.getAddress();
		// ����DLC֡
		EthernetPacket ether = new EthernetPacket();
		ether.frametype = EthernetPacket.ETHERTYPE_ARP;
		ether.src_mac =stomac("74-E5-0B-45-82-7A");
		ether.dst_mac = broad_mac;
		arp.datalink = ether;
		return arp;
	}
	
	public static byte[] stomac(String s) {
		byte[] mac = new byte[] { (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00 };
		String[] s1=null;
		if (s.indexOf("-")>0) {
			s1 = s.split("-");
		}
		else if(s.indexOf(":")>0){
			s1 = s.split(":");
		}
		
		for (int x = 0; x < s1.length; x++) {
			mac[x] = (byte) ((Integer.parseInt(s1[x], 16)) & 0xff);
		}
		return mac;
	}
}
