package util;

public class lsaChecksum {
	
	public static short	ospf_lsa_checksum (byte[] data)
	{

	  /* Skip the AGE field */
	  int len = data.length; 

	  /* Checksum offset starts from "options" field, not the beginning of the
	     lsa_header struct. The offset is 14, rather than 16. */
	  int checksum_offset = 14;

	  return fletcher_checksum(data, len, checksum_offset);
	}
	/* Fletcher Checksum -- Refer to RFC1008. */
	private static final int MODX=4102;   /* 5802 should be fine */
	private static final int FLETCHER_CHECKSUM_VALIDATE=0xffff;
	/* To be consistent, offset is 0-based index, rather than the 1-based 
	   index required in the specification ISO 8473, Annex C.1 */
	/* calling with offset == FLETCHER_CHECKSUM_VALIDATE will validate the checksum
	   without modifying the buffer; a valid checksum returns 0 */
	public static short	fletcher_checksum(byte[] buffer, int len, int offset)
	{
	  byte[] p;
	  int x, y, c0, c1;
	  short checksum;
	  int partial_len, i, left = len;
	  
	  checksum = 0;
	  p = buffer;
	  c0 = 0;
	  c1 = 0;

	  while (left != 0)
	    {
	      partial_len =left<MODX?left:MODX;// MIN(left, MODX);

	      for (i = 0; i < partial_len; i++)
		{
		  c0 = c0 + (p[i]&0xff);
		  c1 += c0;
		}

	      c0 = c0 % 255;
	      c1 = c1 % 255;

	      left -= partial_len;
	    }

	  /* The cast is important, to ensure the mod is taken as a signed value. */
	  x = (int)((len - offset - 1) * c0 - c1) % 255;

	  if (x <= 0)
	    x += 255;
	  y = 510 - c0 - x;
	  if (y > 255)  
	    y -= 255;

	  if (offset == FLETCHER_CHECKSUM_VALIDATE)
	    {
	      checksum = (short) ((c1 << 8) + c0);
	    }
	  else
	    {
	      /*
	       * Now we write this to the packet.
	       * We could skip this step too, since the checksum returned would
	       * be stored into the checksum field by the caller.
	       */
	      buffer[offset] = (byte) x;
	      buffer[offset + 1] = (byte) y;
	      for(int ii=0;ii<len;ii++){
	    	  System.out.printf("%x ",buffer[ii]);
	      }
	      /* Take care of the endian issue */
	      checksum = (short) ((x << 8) | (y & 0xFF));
	    }

	  return checksum;
	}
}
