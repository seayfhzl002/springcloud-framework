package com.pig4cloud.pigx.common.core.util.sign;

import java.nio.charset.Charset;
import java.security.Key;

import java.lang.Exception;


import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DesEncryUtil {

    /**
     * CBC加密
     * @param key   密钥
     * @param keyiv IV
     * @param plainText 明文
     * @param charset 字符集
     * @return Base64编码的密文
     * @throws Exception
     */
    public static String des3EncodeCBC(String key, String keyiv, String plainText, Charset charset) throws Exception {
        byte[] keyByte = key.getBytes(charset);
        byte[] keyivByte = keyiv.getBytes(charset);
        DESKeySpec dks = new DESKeySpec(keyByte);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key deskey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyivByte);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(plainText.getBytes(charset));
        return new String(Base64.encode(bOut),charset);
    }

    /**
     * CBC解密
     * @param key   密钥
     * @param keyiv IV
     * @param cipherText Base64编码的密文
     * @param charset    字符集
     * @return 明文
     * @throws Exception
     */
    public static String des3DecodeCBC(String key, String keyiv, String cipherText, Charset charset) throws Exception {
        byte[] keyByte = key.getBytes(charset);
        byte[] keyivByte = keyiv.getBytes(charset);
        DESKeySpec dks = new DESKeySpec(keyByte);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key deskey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyivByte);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(Base64.decode(cipherText.getBytes(charset)));
        return new String(bOut,charset);
    }
}
