package protocol_packet.OSPF;
/**
 * LSA section following LSR or DD
 * @author CferZ
 *
 */
public class OSPF_LSA_section {
	byte[] ls_age=new byte[2];//first bit for [Do not Age Flag] other 15 bit for LS ages(seconds)
	byte options=0;
	byte ls_type=0;//1 for router lsa ;2 for network lsa
	byte[] link_state_id=new byte[4];
	byte[] adverting_router=new byte[4];
	byte[] sequence=new byte[4];
	byte[] checksum=new byte[2];
	byte[] length=new byte[2];
}
