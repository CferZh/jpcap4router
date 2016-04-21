package routerCapture;

import jpcap.JpcapCaptor;
import jpcap_util.callback_reciever;
import jpcap_util.jpcap_util;

public class main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/**
		 * get incetance of jpcapCaptor
		 * argv: -1 choose netcard
		 * 		 >0 using assigned netcardID
		 * 		on zyf pc 1 means wlan0 3 means reltek
		 */
		jpcap_util jpcap=jpcap_util.getInstance(1);
		//start listen with the callback_receiver provided by argv2
		jpcap.startCaptorWithReciever(new callback_reciever());
		
		System.out.println("wait");
	}

}
