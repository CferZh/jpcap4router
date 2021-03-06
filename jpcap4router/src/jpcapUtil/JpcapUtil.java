package jpcapUtil;

import java.io.IOException;
import java.util.Scanner;

import jpcap.JpcapCaptor;
import jpcap.JpcapSender;
import jpcap.NetworkInterface;
import jpcap.PacketReceiver;
import jpcap.packet.Packet;

public class JpcapUtil {
	private JpcapCaptor captor=null;
	private JpcapSender sender=null;
	private Thread captorThread=null;
	private static JpcapUtil instance=null;
	
	/**
	 * argv: -1 choose netcard
	 * 		 >0 using assigned netcardID
	 * 		on zyf pc 1 means wlan0 3 means reltek
	 * 		on zyf_work_pc 1 means wlan
	 */
	private static int deviceID=3;
	private JpcapUtil(){
		super();
	}
	public static JpcapUtil getInstance(){
		if(instance==null){
			instance=new JpcapUtil();
			//获取网卡设备
			NetworkInterface[] devices = JpcapCaptor.getDeviceList(); 
			System.out.println("选择要使用的网卡:");
			for(int i=0;i<devices.length;i++){
				
				System.out.print(i+" :"
						+ " "+devices[i].description+"\t"
						+devices[i].name+"\t"
						+devices[i].toString()+"\t"	);
				util.util.printHexData(devices[i].mac_address);
			}
			
			deviceID=deviceID>=0?deviceID:new Scanner(System.in).nextInt();
			try {
				//打开网卡
				/** openDevice(NetworkInterface intrface, int snaplen, boolean promics, int to_ms);
				 *  intrface 需要监听的网卡
				 *  snaplen 每次捕获的数据包最大长度（设置为IP包最大长度即可）  \
				 *  romics 是否过滤（Mac地址不是当前网卡的IP数据包）
				 *  to_ms 超时时间
				 */
				instance.sender=JpcapSender.openDevice(devices[deviceID]);
				instance.captor=JpcapCaptor.openDevice(devices[deviceID], 65535, false, 20);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		return instance;
	}
	
	public boolean startCaptorWithReciever(final PacketReceiver receiver){
		boolean is_success=false;
		if(instance==null)
			return is_success;
		if(captorThread==null){
			captorThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(instance!=null){
						instance.captor.loopPacket(-1, receiver);
					}
				}
			});
		}
		captorThread.start();
		is_success=true;
		return is_success;
	}
	public void stopCaptor(){
		captorThread.stop();
	}
	public void closeCaptor(){
		captorThread.stop();
		captor.close();
	}
	public void sendPacket(Packet p){
		if(sender!=null)
			sender.sendPacket(p);
	}
	/**
	 * TODO dinamicly get
	 * @return
	 */
	public static byte[] getMyMac(){
//		byte[] mac={0x74,(byte) 0xe5,0x0b,0x45,(byte) 0x82,0x7a};//无线网卡
//		byte[] mac={(byte) 0xf0,(byte) 0xde,(byte) 0xf1,(byte) 0x8d,0x3f,(byte) 0xda};//有线网卡
	
		byte[] rMac=new byte[6];
		for(int i=0;i<6;i++){
			rMac[i]=((JpcapCaptor.getDeviceList()[deviceID]).mac_address[i]);
		}
		return rMac;
	}
}
