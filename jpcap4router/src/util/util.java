package util;

public class util {
	public static byte[] getchecksum(byte[] buff){
		boolean isNeedPad=false;
		int result=0;
		if(buff.length%2!=0){
			result=PaddingChecksum(buff);
		}
		else{
			result=NoPaddingChecksum(buff);
		}
		byte[] byteResult=new byte[2];
		byteResult[0]=(byte)((result>>8)&0xff);
		byteResult[1]=(byte)(result&0xff);
		return byteResult;
	}
	public static int NoPaddingChecksum(byte[] buff){
		int result=0;
		for(int i=0;i<buff.length;i+=2){
			result=result+((buff[i]&0xff)<<8)+(buff[i+1]&0xff);
		}
		for(;result>65535;result=result>>16+(result&0xffff));
		result=0xffff-result;
		return result;
	}
	public static int PaddingChecksum(byte[] buff){
		int result=0;
		for(int i=0;i<buff.length-1;i+=2){// 16bit section
			result=result+((buff[i]&0xff)<<8)+(buff[i+1]&0xff);
		}
		result+=buff[buff.length-1]<<8;// last 8bit section   padding a 0x00 at last 
		for(;result>65535;result=result>>16+(result&0xffff));
		result=0xffff-result;
		return result;
	}
}
