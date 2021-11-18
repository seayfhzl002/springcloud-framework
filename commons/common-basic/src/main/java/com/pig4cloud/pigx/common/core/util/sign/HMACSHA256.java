package com.pig4cloud.pigx.common.core.util.sign;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * 榴莲支付需要用到
 */
@Slf4j
public class HMACSHA256 {

    //   SECRET KEY
//    private final static String secret_key = "ndE2jdZNFixH9G6Aidsfyf7lYT3PxP";
    private final static String secret_key = "13d05e49-2cd9-46a0-b86e-0e664efd3f0d";

    /**
     * 将加密后的字节数组转换成字符串
     *
     * @param b 字节数组
     * @return 字符串
     */
    private static String byteArrayToHexString(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1) {
                hs.append('0');
            }
            hs.append(stmp);
        }
        return hs.toString().toLowerCase();
    }

    /**
     * sha256_HMAC加密
     * <p>
     * //     * @param message 消息
     *
     * @param secret 秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC(Map<String, Object> packageParams, String secret) {
        String hash = "";
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();
        while (i$.hasNext()) {
            String key = (String) i$.next();
//            String value = (String) packageParams.get(key);
            Object o = packageParams.get(key);
            String value = "";
            if (o instanceof Integer) {
                Integer i = ((Integer) o).intValue();
                value = i.toString();
            } else {
                value = (String) o;
            }
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        toSign = toSign.deleteCharAt(toSign.length() - 1);
        System.out.println("拼接完后=" + toSign);
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(toSign.toString().getBytes());
            hash = byteArrayToHexString(bytes);
            System.out.println("hash=" + hash.toUpperCase());
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash.toUpperCase();
    }

    /**
     * sha256_HMAC加密
     * <p>
     * //     * @param message 消息
     *
     * @param secret 秘钥
     * @return 加密后字符串
     */
    public static String sha256_HMAC2(Map<String, String> packageParams, String secret) {
        String hash = "";
        TreeMap sortedMap = new TreeMap(packageParams);
        StringBuilder toSign = new StringBuilder();
        Iterator i$ = sortedMap.keySet().iterator();
        while (i$.hasNext()) {
            String key = (String) i$.next();
            String value = (String) packageParams.get(key);
//            Object o = packageParams.get(key);
//            String value = "";
//            if (o instanceof Integer) {
//                Integer i = ((Integer) o).intValue();
//                value = i.toString();
//            } else {
//                value = (String) o;
//            }
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key + "=" + value + "&");
            }
        }
        toSign = toSign.deleteCharAt(toSign.length() - 1);
        System.out.println("拼接完后=" + toSign);
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(toSign.toString().getBytes());
            hash = byteArrayToHexString(bytes);
            System.out.println("hash=" + hash.toUpperCase());
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash.toUpperCase();
    }

    public static String sha256_HMAC(JSONObject jsonObject, String secret) {
        Map<String, Object> map = JSONObject.parseObject(jsonObject.toString(), Map.class);
        String sign = sha256_HMAC(map, secret);
        log.info("sign：{}", sign);
        return sign;
    }
}
