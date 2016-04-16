package util;

public class util {
	public byte[] getchecksum(byte[] buff){
		boolean isNeedPad=false;
		int result=0;
		if(buff.length%2!=0){
			isNeedPad=true;
		}
		for(int i=0;i<buff.length;i+=2){
			if(i){
				
			}
			
		}
		return null;
	}
	public int NoPaddingChecksum(byte[] buff){return 0;}
	public int PaddingChecksum(byte[] buff){return 0;}
}
