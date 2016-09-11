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
		for(;result>65535;result=((result>>16)+(result&0xffff)));
		result=0xffff-result;
		return result;
	}
	public static int PaddingChecksum(byte[] buff){
		int result=0;
		for(int i=0;i<buff.length-1;i+=2){// 16bit section
			result=result+((buff[i]&0xff)<<8)+(buff[i+1]&0xff);
		}
		result+=buff[buff.length-1]<<8;// last 8bit section   padding a 0x00 at last 
		for(;result>65535;result=((result>>16)+(result&0xffff)));
		result=0xffff-result;
		return result;
	}
	public static void printHexData(byte[] data){
		for(int i=0;i<data.length;i++){
			System.out.printf("%x ",data[i]);
		}
		System.out.println();
	}
	public static int parseByte2Int(byte[] binary){
		int result=0;
		for(int i=0;i<4;i++){
			result+=((binary[i]&0xff)<<(24-8*i));
		}
		return result;
	}
	public static short parseByte2Short(byte[] binary){
		short result=0;
		result=(short) ((binary[0]&0xff)<<8);
		result+=binary[1]&0xff;
		
		return result;
	}
	public static byte[] parseInt2Byte(int num){
		byte[] result=new byte[4];
		for(int i=0;i<4;i++){
			result[i]=(byte) ((num>>(24-8*i))&0xff);
		}
		return result;
	}
}
