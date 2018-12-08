package com.student.util;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


/**
 * 双向加密 对称加密 AES算法
 */
public class AESUtils {

    private static String default_key ="67d73cec5d6b4c8a8a9883748f4066fe"; //4cff7f55513eaa64506fe2e6ec3555da
    private static String default_iv = "user-service-api"; //IV length: must be 16 bytes long


    /**
     * AES加密
     *
     * @param data 要加密的字符串
     * @param key  key值必须为Const.APPSECRET，key为null时，默认使用Const.APPSECRET
     * @param iv   密码加密算法中的IV
     * @return 加密后的字符串
     */
    public static String encrypt(String data, String key, String iv) {
        try {
            if (StringUtils.isBlank(key)) {
                key = default_key.substring(0, 16);
            } else {
                key = key.substring(0, 16);
            }
            if (StringUtils.isBlank(iv)) {
                iv = default_iv;
            }
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }
            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            String encrypt = Base64.getEncoder().encodeToString(encrypted); //BASE64加密
            encrypt = encrypt.replaceAll(new String("\r"), "");
            encrypt = encrypt.replaceAll(new String("\n"), "");
            return encrypt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES加密
     *
     * @param data 要加密的字符串
     * @param key  key值必须为Const.APPSECRET，key为null时，默认使用Const.APPSECRET
     * @return 加密后的字符串
     */
    public static String encrypt(String data, String key) {
        return encrypt(data, key, default_iv);
    }

    /**
     * AES加密
     *
     * @param data 要加密的字符串
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {
        return encrypt(data, default_key);
    }


    /**
     * AES解密
     *
     * @param data 加密后的字符串
     * @param key  key值必须为Const.APPSECRET，key为null时，默认使用Const.APPSECRET
     * @param iv   密码加密算法中的IV
     * @return 解密后的字符串
     */
    public static String decrypt(String data, String key, String iv) {
        try {
            if (StringUtils.isBlank(key)) {
                key = default_key.substring(0, 16);
            } else {
                key = key.substring(0, 16);
            }
            if (StringUtils.isBlank(iv)) {
                iv = default_iv;
            }
            byte[] encrypted1 = Base64.getDecoder().decode(data); //BASE64解密
            System.out.println("AES加密码：" + new String(encrypted1));
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param data 加密后的字符串
     * @param key  key值必须为Const.APPSECRET，key为null时，默认使用Const.APPSECRET
     * @return 解密后的字符串
     */
    public static String decrypt(String data, String key) {
        return decrypt(data, key, default_iv);
    }

    /**
     * AES解密
     *
     * @param data 加密后的字符串
     * @return 解密后的字符串
     */
    public static String decrypt(String data) {
        return decrypt(data, default_key);
    }

    public static void main(String[] args) {
        String data = "ADMIN123manager"; //l0u90rzk5WYCnEvrMLfmqA==
        //String key = "67d73cec5d6b4c8a8a9883748f4066fe";
        String key = default_key;
        try {
            System.out.println("加密前：" + data);
            String a = encrypt(data);
            System.out.println("加密后：" + a);
            System.out.println("直接解密：" + decrypt(a));
            String b = decrypt("l0u90rzk5WYCnEvrMLfmqA==");
            System.out.println("直接对加密字符串l0u90rzk5WYCnEvrMLfmqA==解密\n" + b);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}