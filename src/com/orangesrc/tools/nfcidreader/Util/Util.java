package com.orangesrc.tools.nfcidreader.Util;


/**
 * This helper class contains various utility methods
 * 
 * @author jack
 *
 */
public class Util {	
	// TAG for use throughout the application
	public static final String TAG = "NFCIDreader";

	/**
	 * Simple util method to convert a byte array to a hex string
	 * 
	 * @param in byte array to convert
	 * @return hex string representation of byte array
	 */
	public static String ConvertbyteArrayToHexString(byte in[]) {    	
		byte ch = 0x00;
		int i = in.length-1;

		if (in == null) {
			return null;
		}

		String HEXSET[] = {"0", "1", "2","3", "4", "5", "6", "7", "8","9", "A", "B", "C", "D", "E","F"};
		
		//Double length, as you're converting an array of 8 bytes, to 16 characters for hexadecimal
		StringBuffer out = new StringBuffer(in.length * 2);

		//You need to iterate from msb to lsb, in the case of using iCode SLI rfid
		while (i >= 0) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4); // shift the bits down
			ch = (byte) (ch & 0x0F); // must do this is high order bit is on!
			out.append(HEXSET[ (int) ch]); // convert the nibble to a String Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble
			out.append(HEXSET[ (int) ch]); // convert the nibble to a String Character
			i--;
		}
		return (new String(out));
	}
}
