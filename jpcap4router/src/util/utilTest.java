package util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class utilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetchecksum() {
		byte[] buff={0x45,(byte)0xc0,0x00,0x4c,0x00,0x33,0x00,0x00,0x01,0x59,0x0a,0x00,0x00,0x03,(byte)0xe0,0x00,0x00,0x05 };
		byte[] result=util.getchecksum(buff);
		for(int i=0;i<2;i++){
			System.out.printf("%x",result[i]);
		}
		fail("Not yet implemented");
	}

	@Test
	public void testNoPaddingChecksum() {
		fail("Not yet implemented");
	}

	@Test
	public void testPaddingChecksum() {
		fail("Not yet implemented");
	}

}
