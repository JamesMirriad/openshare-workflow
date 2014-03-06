package com.mirriad.util.base64;

import org.apache.commons.codec.binary.Base64;

/**
 * utility class for base 64 operations
 * @author james.mcilroy
 *
 */
public class Base64Util {

	/**
	 * 
	 * This method take base64 type string as input and
	 * convert it to byte[] array
	 *  
	 * @param data base64 type string
	 * @return byte array
	 * @throws Exception
	 *  
	 */
	@SuppressWarnings("unused")
	public static byte[] base64ToByte(String data) throws Exception {
	 Base64 base64 = new Base64();
	 return Base64.decodeBase64(data);
	}

	/**
	 * This method take byte array as input and
	 * convert it to base64 string
	 * 
	 * @param data byte array
	 * @return base64 string
	 * 
	 */
	@SuppressWarnings("static-access")
	public static String byteToBase64(byte[] data) {
	 Base64 base64 = new Base64();
	 return base64.encodeBase64String(data);
	}

}
