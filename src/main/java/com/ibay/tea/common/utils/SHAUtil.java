package com.ibay.tea.common.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * SHA加密工具类
 */
@Slf4j
public class SHAUtil {

    private static final String DES = "DES";
    private static final String DEFAULT_ENCODING = "UTF-8";
    /**
     *  利用java原生的摘要实现SHA2-1加密
     * @param data 加密的报文
     * @return
     */
    public static byte[] getSHA1Digest(String data) throws IOException {
        byte[] bytes = null;
        try{
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            bytes = sha.digest(data.getBytes(DEFAULT_ENCODING));
        }catch(NoSuchAlgorithmException e){
            log.info("get SHA1 Digest error, ", e);
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 将byte转为16进制
     * @param bytes
     * @return
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sign.append(Integer.toString( ( bytes[i] & 0xff ) + 0x100, 16).substring( 1 ));
        }
        return sign.toString();
    }

    /**
     * 密码加密
     * @param password
     * @return
     * @throws Exception
     */
    public final static String encrypt(String data, String password) {
        try {
            return byte2hex(encrypt(data.getBytes(DEFAULT_ENCODING),
                    password.getBytes(DEFAULT_ENCODING)));
        } catch (Exception e) {
            log.error("encrypt error", e);
        }
        return null;
    }

    /**
     * 密码解密
     * @param data
     * @return
     * @throws Exception
     */
    public final static String decrypt(String data, String password) {
        try {
            return new String(decrypt(hexTobyte(data.getBytes(DEFAULT_ENCODING)),
                    password.getBytes(DEFAULT_ENCODING)),DEFAULT_ENCODING);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 加密
     * @param src  数据源
     * @param key  密钥，长度必须是8的倍数
     * @return 返回加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成加密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        // 现在，获取数据并加密
        // 正式执行加密操作
        return cipher.doFinal(src);
    }

    /**
     * 解密
     * @param src  数据源
     * @param key  密钥，长度必须是8的倍数
     * @return 返回解密后的原始数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws Exception {
        // DES算法要求有一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        // 从原始密匙数据创建一个DESKeySpec对象
        DESKeySpec dks = new DESKeySpec(key);
        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
        // 一个SecretKey对象
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        // Cipher对象实际完成解密操作
        Cipher cipher = Cipher.getInstance(DES);
        // 用密匙初始化Cipher对象
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        // 现在，获取数据并解密
        // 正式执行解密操作
        return cipher.doFinal(src);
    }

    /**
     * 二行制转字符串
     * @param b
     * @return
     */
    public static String byteTohex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    public static byte[] hexTobyte(byte[] b) {
        try{
            if ((b.length % 2) != 0) {
                throw new IllegalArgumentException("长度不是偶数");
            }
            byte[] b2 = new byte[b.length / 2];
            for (int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2,"UTF-8");
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }
            return b2;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
