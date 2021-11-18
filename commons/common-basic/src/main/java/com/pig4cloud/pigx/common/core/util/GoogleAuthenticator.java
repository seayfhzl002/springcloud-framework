package com.pig4cloud.pigx.common.core.util;

import com.pig4cloud.pigx.common.core.exception.ValidateCodeException;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * 谷歌验证器通用类
 */
public class GoogleAuthenticator {

    // taken from Google pam docs - we probably don't need to mess with these
    public static final int SECRET_SIZE = 10;

    public static final String SEED = "g8GjEvTbW5oVSV7avLBdwIHqGlUYNzKFI7izOF8GwLDVKs2m0QN7vxRs2im5MDaNCWGmcD2rvcZx";

    public static final String RANDOM_NUMBER_ALGORITHM = "SHA1PRNG";

    public static final String HOST = "zongheng.org";

    int windowSize = 1; // default 3 - max 17 (from google docs)最多可偏移的时间

    public void setWindowSize(int s) {
        if (s >= 1 && s <= 17) {
            windowSize = s;
        }
    }

    @SneakyThrows
    public static Boolean authcode(String codes, String savedSecret) {
        // enter the code shown on device. Edit this and run it fast before the
        // code expires!

        long code = 0;
        try {
            code = Long.parseLong(codes);
        } catch (NumberFormatException e) {
            throw new ValidateCodeException("谷歌验证码错误");
        }
        long t = System.currentTimeMillis();
        GoogleAuthenticator ga = new GoogleAuthenticator();
        ga.setWindowSize(1); // should give 3 * 30 seconds of grace...
        return ga.checkCode(savedSecret, code, t);
    }

//    public static void main(String[] args) {
//        String secret = GoogleAuthenticator.generateSecretKey();
//        String url = GoogleAuthenticator.getQRBarcodeURL("admin", secret);
//        System.out.println(secret);
//        System.out.println(url);
//        System.out.println(GoogleAuthenticator.authcode("455397", "EJVHCZHLKDB5S4PH"));
//    }

    public static String generateSecretKey() {
        SecureRandom sr;
        try {
            sr = SecureRandom.getInstance(RANDOM_NUMBER_ALGORITHM);
            sr.setSeed(Base64.decodeBase64(SEED));
            byte[] buffer = sr.generateSeed(SECRET_SIZE);
            Base32 codec = new Base32();
            byte[] bEncodedKey = codec.encode(buffer);
            return new String(bEncodedKey);
        } catch (NoSuchAlgorithmException ignored) {

        }
        return null;
    }

    public static String getQRBarcodeURL(String user, String secret) {
        String format = "https://api.qrserver.com/v1/create-qr-code/?data=otpauth://totp/%s@%s%%3Fsecret%%3D%s";
        return String.format(format, user, HOST, secret);
    }

    public boolean checkCode(String secret, long code, long timeMsec) {
        Base32 codec = new Base32();
        byte[] decodedKey = codec.decode(secret);
        // convert unix msec time into a 30 second "window"
        // this is per the TOTP spec (see the RFC for details)
        long t = (timeMsec / 1000L) / 30L;
        // Window is used to check codes generated in the near past.
        // You can use this value to tune how far you're willing to go.
        for (int i = -windowSize; i <= windowSize; ++i) {
            long hash;
            try {
                hash = verifyCode(decodedKey, t + i);
            }catch (Exception e) {
                // Yes, this is bad form - but
                // the exceptions thrown would be rare and a static configuration problem
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
                //return false;
            }
            if (hash == code) {
                return true;
            }
        }
        // The validation code is invalid.
        return false;
    }

    private static int verifyCode(byte[] key, long t) throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] data = new byte[8];
        long value = t;
        for (int i = 8; i-- > 0; value >>>= 8) {
            data[i] = (byte) value;
        }
        SecretKeySpec signKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signKey);
        byte[] hash = mac.doFinal(data);
        int offset = hash[20 - 1] & 0xF;
        // We're using a long because Java hasn't got unsigned int.
        long truncatedHash = 0;
        for (int i = 0; i < 4; ++i) {
            truncatedHash <<= 8;
            // We are dealing with signed bytes:
            // we just keep the first byte.
            truncatedHash |= (hash[offset + i] & 0xFF);
        }
        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= 1000000;
        return (int) truncatedHash;
    }
}