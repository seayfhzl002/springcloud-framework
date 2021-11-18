package com.pig4cloud.pigx.common.core.util;

import cn.hutool.crypto.digest.MD5;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

public class EncryptUtil {

	private static final String AES_KEY = "a0caa1de8d1b46b1b13e5943acd8b546";

	private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

	public static String base64Encode(byte[] bytes){
		return Base64.encodeBase64String(bytes);
	}

	public static byte[] base64Decode(String base64Code) {
		return Base64.decodeBase64(base64Code);
	}

	public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(MD5.create().digestHex(encryptKey, "UTF-8").toLowerCase().getBytes(), "AES"));
		return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
	}

	public static String aesEncrypt(String content) throws Exception {
		return base64Encode(aesEncryptToBytes(content, AES_KEY));
	}

	public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
		cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(MD5.create().digestHex(decryptKey, "UTF-8").toLowerCase().getBytes(), "AES"));
		byte[] decryptBytes = cipher.doFinal(encryptBytes);
		return new String(decryptBytes);
	}

	public static String aesDecrypt(String encryptStr) throws Exception {
		return aesDecryptByBytes(base64Decode(encryptStr), AES_KEY);
	}

	public static void main(String[] args) throws Exception {
		String a = "";
		String b = java.util.Base64.getEncoder().encodeToString(aesEncrypt(a).getBytes());
		System.out.println(b);
		String c = aesDecrypt(new String(java.util.Base64.getDecoder().decode("")));
		System.out.println(c);

//		String jsonValue = JSONObject.toJSONString("aze123",
//				SerializerFeature.WriteMapNullValue,
//				SerializerFeature.WriteNullBooleanAsFalse,
//				SerializerFeature.WriteNullStringAsEmpty,
//				SerializerFeature.WriteNullListAsEmpty,
//				SerializerFeature.DisableCircularReferenceDetect);
//		System.out.println(jsonValue);
	}


}